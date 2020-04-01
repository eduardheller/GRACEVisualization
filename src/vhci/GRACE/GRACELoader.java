/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GRACE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import vhci.GL.GLScene.GLExternalSceneAccess;

/**
 *
 * @author Eduard
 */
public class GRACELoader implements GRACEFileLoader{
    
    
    private final int LONGTITUDE_COUNT = 360;
    private final int LATITUDE_COUNT = 180;
    private final int START_OF_DATA_OCEAN = 24;
    private final int START_OF_DATA_LAND = 21;
    private final int START_OF_DATA_SCALE_LAND = 13;
    private final int LAND_SCALE_INDEX = 2;
    
    public final static float MIN_RANGE = -20;
    public final static float MAX_RANGE = 20;
    
    private final String LAND_DIRECTORY = "/land";
    private final String OCEAN_DIRECTORY = "/ocean";
    
    private List<String> landFilePaths = new ArrayList<>();
    private List<String> oceanFilePaths = new ArrayList<>();

    // Die Daten beginnen erst ab dem vierten File
    private int currentLandIndex = 3;
    // Die Daten beginnen ab dem ersten File
    private int currentOceanIndex = 0;
    
    private int currentGraceFileIndex = 0;
    
    public GRACEData currentGRACEData;
    public GRACEData oldGRACEData;
    
    public List<GRACEData> graceData = new ArrayList<>();
    

    public GRACELoader(String pathToGraceFiles) 
    {
        try {
            landFilePaths = getFilesFromDirectory(pathToGraceFiles+LAND_DIRECTORY);
            oceanFilePaths = getFilesFromDirectory(pathToGraceFiles+OCEAN_DIRECTORY);
        } catch (IOException ex) {
            Logger.getLogger(GRACELoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void loadNextFile() {

        try {

            graceData.add(new GRACEData(getDate(),LoadGraceData(
                    landFilePaths.get(currentLandIndex),
                    oceanFilePaths.get(currentOceanIndex)
            )));
            
            currentLandIndex++;
            currentOceanIndex++;
            
            currentGRACEData = graceData.get(currentGraceFileIndex);
            oldGRACEData = graceData.get(currentGraceFileIndex);
            
        } catch (IOException ex) {
            Logger.getLogger(GRACELoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    @Override
    public int graceFileCount() {
        return oceanFilePaths.size();
    }
    
    @Override
    public String getGraceFileName(int index) {
        return landFilePaths.get(index+3) + ", "+ oceanFilePaths.get(index);
    }
    
    
    @Override
    public boolean next()
    {
        boolean hasNext = true;
        oldGRACEData = graceData.get(currentGraceFileIndex);
        currentGraceFileIndex++;
        if(currentGraceFileIndex>=graceData.size()-1) {
            currentGraceFileIndex--;
            hasNext = false;
        }
        currentGRACEData = graceData.get(currentGraceFileIndex);
        
        return hasNext;
    }
    
    @Override
    public boolean setCurrentData(int index)
    {
        if(index>=graceData.size()-1) return false;
        
        oldGRACEData = graceData.get(index);
        currentGraceFileIndex = index;        
        currentGRACEData = graceData.get(currentGraceFileIndex);        
        return true;
    }

    @Override
    public boolean previous()
    {
        boolean hasPrevious = true;
        oldGRACEData = graceData.get(currentGraceFileIndex);
        currentGraceFileIndex--;
        if(currentGraceFileIndex<0) {
            currentGraceFileIndex++;
            hasPrevious = false;
            
        }
    
        currentGRACEData = graceData.get(currentGraceFileIndex);
        return hasPrevious;
    }
    
    @Override
    public GRACEData getCurrentGraceData() {
        return currentGRACEData;
    }
    
    @Override
    public GRACEData getPreviousGraceData() {
        return oldGRACEData;
    }


    @Override
    public float getFillValue() {
        return 32767f;
    }
    
    
    @Override
    public String getDate()
    {
        Path path = Paths.get(oceanFilePaths.get(currentOceanIndex));
        String date = path.getFileName().toString().split("\\.")[2];
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        return day+"."+month+"."+year;
    }
    
    @Override
    public LinkedHashMap<String,Float> getWeightDataFromLongLat(int longtitude, int latitude)
    {
        LinkedHashMap<String,Float> map = new LinkedHashMap<String,Float>();
        for(int i=0; i<graceData.size(); i++)
        {
            map.put(graceData.get(i).date, graceData.get(i).data.get(new GRACECoordinate(longtitude, latitude)));

        }
        return map;
    }
    
    
    private List<String> getFilesFromDirectory(String directory) throws IOException
    {
        List<String> paths = new ArrayList<>();
        Files.walk(Paths.get(directory)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                String file = filePath.getFileName().toString();
                String ext = file.substring(file.lastIndexOf("."));
                paths.add(filePath.toString());
            }
        });
        
        return paths;
    }
    
    
    private HashMap<GRACECoordinate,Float> LoadGraceData(String path,String pathOcean) throws IOException{
        HashMap<GRACECoordinate,Float> data = new HashMap<>(LONGTITUDE_COUNT*LATITUDE_COUNT);
        data.putAll(LoadFile(START_OF_DATA_LAND, path));
        data.putAll(LoadFile(START_OF_DATA_OCEAN, pathOcean)); 
        return data;
    }
  
    private HashMap<GRACECoordinate,Float> LoadScaleLandData(String path) throws IOException{
        HashMap<GRACECoordinate,Float> data = new HashMap<>(LONGTITUDE_COUNT*LATITUDE_COUNT);
        data.putAll(LoadFile(START_OF_DATA_SCALE_LAND, path));
        return data;
    }
    
    private HashMap<GRACECoordinate,Float> LoadFile(int startLineOfFile, String path) throws IOException
    {
        HashMap<GRACECoordinate,Float> data = new HashMap<>(LONGTITUDE_COUNT*LATITUDE_COUNT);
        int lineNumber = 0;
        String line;
        try {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(path));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(gzip));

            while ((bufferedReader.readLine()) != null) { 
                lineNumber++;
                // Erst ab den Daten für die Erdschwere Daten füllen, beginnend ab Zeile 23
                if(lineNumber>startLineOfFile) break;
            } 

            while ((line = bufferedReader.readLine()) != null) { 
                String[] values = line.split("\\s+");   
                int longt = (int)Float.parseFloat(values[1]);
                int lat = (int)Float.parseFloat(values[2])+90;
                float weight = Float.parseFloat(values[3]);
                data.put(
                    new GRACECoordinate(longt, lat),
                    weight);
            }    

            bufferedReader.close();
            gzip.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GLExternalSceneAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    @Override
    public int getCurrentGRACEIndex() {
        return currentGraceFileIndex;
    }




}
