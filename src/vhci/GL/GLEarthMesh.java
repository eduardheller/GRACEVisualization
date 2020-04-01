/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.system.MemoryUtil;

import vhci.GRACE.GRACECoordinate;
import vhci.GRACE.GRACEFileLoader;
import vhci.GRACE.GRACELoader;

/**
 *
 * @author Eduard
 */
public class GLEarthMesh extends GLMesh{
    
    private final List<Float> weights = new ArrayList<>();
    private final List<Vector3f> verticesTarget = new ArrayList<>();
    private final List<Vector3f> normalsTarget = new ArrayList<>();
    private final List<Float> weightsTarget = new ArrayList<>();

    private final float radius = 1.0f;
    private final int STEP = 1;
    private final int MAX_LONGTITUDE = 360;
    private final int MAX_LATITUDE = 180;
    private final float MIN_LONGTITUDE = 0.5f;
    private final float MIN_LATITUDE = 0.5f;

    private final int VBOWeights;
    private final int VBOTargets;
    
    public final float latitudeBands = 179;
    public final float longitudeBands = 359;
    
    private GLTimerAnimation timerAnimation;

    private final GRACEFileLoader grace;
    
    public GLEarthMesh(GRACEFileLoader graceLoader, GLTimerAnimation timerAnim)
    {
        
        super();
        
        VBOWeights = glGenBuffers();
        VBOTargets = glGenBuffers();
        createArrays();
        initSphereIndices();
        grace = graceLoader;
        timerAnimation = timerAnim;
        
        
    }
    
    @Override
    public void initBufferObjects() {
        
        initWeightsBuffer();
        super.initBufferObjects();
        if(timerAnimation.lerp) timerAnimation.setDefaultTimer();
    }
    
    @Override
    public void initMeshData() {

        initWeights();
        initVertices();
        calculateNormals();
    }



    @Override
    public void cleanUp()
    {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(VBOWeights);
        glDeleteBuffers(VBOTargets);
        super.cleanUp();
    }
    
    private void initVertices()
    {
        int weightDataIndex = 0;
        normals.clear();
        normalsTarget.clear();
        vertices.clear();
        verticesTarget.clear();
        
        
        for (float latNumber = 0; latNumber <= latitudeBands; latNumber++) {
            
            float theta = latNumber * (float)Math.PI / latitudeBands;
            float sinTheta = (float)Math.sin(theta);
            float cosTheta = (float)Math.cos(theta);

            for (float longNumber = 0; longNumber <= longitudeBands; longNumber++) {
                
                float phi = longNumber * 2 * (float)Math.PI / longitudeBands;
                float sinPhi = (float)Math.sin(phi);
                float cosPhi = (float)Math.cos(phi);

                Vector3f normal = new Vector3f(cosPhi * sinTheta, cosTheta, sinPhi * sinTheta);
                normals.add(normal);
                
                Vector3f pos = new Vector3f(-radius*normal.x, -radius*normal.y, radius*normal.z);
                
                float displacement = Math.min(Math.max( -1*weights.get(weightDataIndex), GRACELoader.MIN_RANGE ), GRACELoader.MAX_RANGE );
                
                float distance = Math.abs(GRACELoader.MAX_RANGE)+Math.abs(GRACELoader.MIN_RANGE);
                
                displacement = 1.11f - (displacement+GRACELoader.MAX_RANGE) / distance / 3f / 3f;
                
                if(weights.get(weightDataIndex)>32000) displacement = 1.1f;
                vertices.add(pos.mul(displacement)); 
                uvs.add(new Vector2f(1 - ((longNumber-180) / longitudeBands),((latNumber)/ latitudeBands)));
                
                Vector3f posTarget = new Vector3f(-radius*normal.x, -radius*normal.y, radius*normal.z);
                displacement = Math.min(Math.max( -1*weightsTarget.get(weightDataIndex), GRACELoader.MIN_RANGE ), GRACELoader.MAX_RANGE );
                displacement = 1.11f - (displacement+GRACELoader.MAX_RANGE) / distance / 3f / 3f;
                if(weightsTarget.get(weightDataIndex)>32000) displacement = 1.1f;
                verticesTarget.add(posTarget.mul(displacement)); 
                normalsTarget.add(normal);
                weightDataIndex++;
            }
        }
        

    }
    
    
    private void initWeights()
    {
        
        weights.clear();
        weightsTarget.clear();
        
        for (float latNumber = 0; latNumber <= latitudeBands; latNumber++) {
            for (float longNumber = 0; longNumber <= longitudeBands; longNumber++) {
                int dataLongtitude = (int) ((int)longNumber % longitudeBands);
                int dataLatitude = (int) ((int)latNumber % latitudeBands);  
                GRACECoordinate coord = new GRACECoordinate(dataLongtitude, dataLatitude);
                   
                HashMap<GRACECoordinate, Float> graceCoords = grace.getPreviousGraceData().data;
                

                if(graceCoords.containsKey(coord)) 
                {
                    weights.add(graceCoords.get(coord));
                }   
                else
                {
                    weights.add(grace.getFillValue());
                }

                HashMap<GRACECoordinate, Float> graceCoordsTarget = grace.getCurrentGraceData().data;

                if(graceCoordsTarget.containsKey(coord)) 
                {
                    weightsTarget.add(graceCoordsTarget.get(coord));

                }   
                else
                {
                    weightsTarget.add(grace.getFillValue());
                }
            }
       
        }
    }
    
    public void initSphereIndices()
    {        
        indices.clear();
        for (int latNumberN = 0; latNumberN < latitudeBands; latNumberN++) {
            for (int longNumberN = 0; longNumberN < longitudeBands; longNumberN++) {
                int first = (latNumberN * ((int)longitudeBands + 1)) + longNumberN;
                int second = first + (int)longitudeBands + 1;

                indices.add(first);
                indices.add(second);
                indices.add(first + 1);

                faces.add(new GLFace(first,second,first+1));
                
                indices.add(second);
                indices.add(second + 1);
                indices.add(first + 1);
                
                faces.add(new GLFace(second,second+1,first+1));
                
            }
        }
    }
    
    private void calculateNormals()
    {
          
        for( int i=0; i < faces.size(); i++ )
        {
            int ia = faces.get(i).v1;
            int ib = faces.get(i).v2;
            int ic = faces.get(i).v3;
            
            Vector3f e1 = new Vector3f();
            vertices.get(ia).sub(vertices.get(ib),e1);
            Vector3f e2 = new Vector3f();
            vertices.get(ic).sub(vertices.get(ib),e2);
            
            Vector3f no = e1.cross(e2);
            
            normals.set(ia, no);
            normals.set(ib, no);
            normals.set(ic, no); 
            
            e1 = new Vector3f();
            e2 = new Vector3f();
            
            verticesTarget.get(ia).sub(verticesTarget.get(ib),e1);
            verticesTarget.get(ic).sub(verticesTarget.get(ib),e2);
            no = new Vector3f();
            no = e1.cross(e2);
            
            normalsTarget.set(ia, no);
            normalsTarget.set(ib, no);
            normalsTarget.set(ic, no); 
        }
        
        

        for( int i=0; i < normals.size(); i++ )
        {
            normals.get(i).normalize();
            normalsTarget.get(i).normalize();
        }
        
    }
    

    
    private void initWeightsBuffer()
    {
        FloatBuffer weightsBuffer = null;
        FloatBuffer verticesTargetsBuffer = null;
        try
        {
            weightsBuffer = MemoryUtil.memAllocFloat(weights.size()*2);
            
            for( int i=0; i< weights.size(); i++){
                weightsBuffer.put(weights.get(i));  
                weightsBuffer.put(weightsTarget.get(i));  
                
            }
            weightsBuffer.flip();
            
            verticesTargetsBuffer = MemoryUtil.memAllocFloat(verticesTarget.size() * 6);
            
            for( int i=0; i< verticesTarget.size(); i++){
                
                verticesTargetsBuffer.put(verticesTarget.get(i).x);             
                verticesTargetsBuffer.put(verticesTarget.get(i).y);
                verticesTargetsBuffer.put(verticesTarget.get(i).z);                
                verticesTargetsBuffer.put(normalsTarget.get(i).x);             
                verticesTargetsBuffer.put(normalsTarget.get(i).y);
                verticesTargetsBuffer.put(normalsTarget.get(i).z);
            }
            
            verticesTargetsBuffer.flip();
            

            glBindVertexArray(getVaoId());
            glBindBuffer(GL_ARRAY_BUFFER, VBOWeights);
            glBufferData(GL_ARRAY_BUFFER, weightsBuffer, GL_STATIC_DRAW);  
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTargets);
            glBufferData(GL_ARRAY_BUFFER, verticesTargetsBuffer, GL_STATIC_DRAW);  
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);     
         

        } finally {
            if (weightsBuffer  != null) MemoryUtil.memFree(weightsBuffer);
            if (verticesTargetsBuffer  != null) MemoryUtil.memFree(verticesTargetsBuffer);
        }
        
    }

    
    private void createArrays()
    {
        glBindVertexArray(getVaoId());
        glBindBuffer(GL_ARRAY_BUFFER, VBOWeights);
        glVertexAttribPointer(3, 1, GL_FLOAT, false, SIZE_OF_FLOAT*2, 0);
        glVertexAttribPointer(6, 1, GL_FLOAT, false, SIZE_OF_FLOAT*2, SIZE_OF_FLOAT);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(6);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, VBOTargets);
        glVertexAttribPointer(4, 3, GL_FLOAT, false, SIZE_OF_FLOAT*6, 0);
        glVertexAttribPointer(5, 3, GL_FLOAT, false, SIZE_OF_FLOAT*6, SIZE_OF_FLOAT*3);
        glEnableVertexAttribArray(4);
        glEnableVertexAttribArray(5);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
}
