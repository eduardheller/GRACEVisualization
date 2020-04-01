/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


/**
 *
 * @author rohdej
 */
public abstract class GLMesh {
    
    private int VAO;
    private int VBO;
    private int IBO;
    
    protected List<Vector3f> vertices = new ArrayList<>();
    protected List<Integer> indices = new ArrayList<>();
    protected List<Vector2f> uvs = new ArrayList<>();
    protected List<Vector3f> normals = new ArrayList<>();
    protected List<GLFace> faces = new ArrayList<>();
    
    private FloatBuffer verticesBuffer = null;
    private IntBuffer indiceBuffer = null;
    public boolean changeData = false;
   
    protected final int SIZE_OF_FLOAT = Float.SIZE / Byte.SIZE;
    

    public GLMesh()
    {
        createArrays();
    }
    
    
    public void init()
    {
        initMeshData();
        initBufferObjects();
        changeData = false;
        
    }
    
    
    public int getVaoId() {
        return VAO;
    }

    public int getVertexCount() {
        return vertices.size();
    }
    
    public int getIndicesCount() {
        return indices.size();
    }
    
    protected abstract void initMeshData();
    
    public void initBufferObjects() {
        

        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.size() * 8);
            indiceBuffer = MemoryUtil.memAllocInt(indices.size());
            
            for( int i=0; i< vertices.size(); i++){
                verticesBuffer.put(vertices.get(i).x);             
                verticesBuffer.put(vertices.get(i).y);
                verticesBuffer.put(vertices.get(i).z);
                verticesBuffer.put(uvs.get(i).x);
                verticesBuffer.put(uvs.get(i).y);
                verticesBuffer.put(normals.get(i).x);             
                verticesBuffer.put(normals.get(i).y);
                verticesBuffer.put(normals.get(i).z);   

            }
            
            verticesBuffer.flip();
            
            for( int i=0; i< indices.size(); i++){
                indiceBuffer.put(indices.get(i));             
            }

            indiceBuffer.flip();
           
            glBindVertexArray(VAO);
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0); 
           
            
        } finally {
            if (verticesBuffer  != null) MemoryUtil.memFree(verticesBuffer);
            if (indiceBuffer  != null) MemoryUtil.memFree(indiceBuffer);
        } 
    }

    public void cleanUp() {
        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(VBO);
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(VAO);
    }
    
    public List<Vector3f> getVertices() {
        return vertices;
    }
   
    private void createArrays()
    {
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        VBO = glGenBuffers();
        IBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, SIZE_OF_FLOAT*8, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, SIZE_OF_FLOAT*8, SIZE_OF_FLOAT*3);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, SIZE_OF_FLOAT*8, SIZE_OF_FLOAT*5);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    

}
