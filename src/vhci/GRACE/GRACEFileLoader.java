/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GRACE;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Eduard
 */
public interface GRACEFileLoader {

    public void loadNextFile();
    public int graceFileCount();
    public String getGraceFileName(int index);
    public LinkedHashMap<String,Float> getWeightDataFromLongLat(int longtitude, int latitude);
    public boolean next();
    public boolean previous();
    public int getCurrentGRACEIndex();
    public String getDate();
    public GRACEData getCurrentGraceData();
    public GRACEData getPreviousGraceData();
    public float getFillValue();
    public boolean setCurrentData(int index);
}
