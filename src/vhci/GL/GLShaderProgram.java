/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import org.lwjgl.system.MemoryStack;


/**
 *
 * @author rohdej
 */
public class GLShaderProgram {
    
    private int ShaderProgram;
    private Map<String, Integer> uniforms;

    public GLShaderProgram() {
        uniforms = new HashMap<>();
    }
    
    public void startShader(){
        glUseProgram(ShaderProgram);
    }
    
    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(ShaderProgram, uniformName);        
        uniforms.put(uniformName, uniformLocation);
    }   
    
    public void setUniform(String uniformName, Matrix4f value) {
    
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        } 
        catch(Exception e)
        {
                throw new IllegalArgumentException("", e);
        }
    }
    
    public void setUniform(String uniformName, Vector3f value) {    
        GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }
    
    public void setUniform(String uniformName, float value) {    
        GL20.glUniform1f(uniforms.get(uniformName),value);
    }
    
    public void setUniform(String uniformName, int value) {    
        GL20.glUniform1i(uniforms.get(uniformName),value);
    }
    
    public boolean loadShader(String vertexpath, String fragmentPath){
        ShaderProgram = glCreateProgram();

        int vertShader = loadAndCompileShader(vertexpath, GL_VERTEX_SHADER);
        int fragShader = loadAndCompileShader(fragmentPath, GL_FRAGMENT_SHADER);

        glAttachShader(ShaderProgram, vertShader);
        glAttachShader(ShaderProgram, fragShader);
        glLinkProgram(ShaderProgram);
        
        return true;
    }
            
    private int loadAndCompileShader(String filename, int shaderType)
    {
        
        int handle = glCreateShader(shaderType);
        String code = loadFile(filename);

        glShaderSource(handle, code);
        glCompileShader(handle);

        int shaderStatus = GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS);
        if( shaderStatus == GL11.GL_FALSE)
        {
                throw new IllegalStateException("compilation error for shader ["+filename+"]. Reason: " + glGetShaderInfoLog(handle, 1000));
        }

        return handle;
    }

    private String loadFile(String filename)
    {
        StringBuilder vertexCode = new StringBuilder();
        String line = null ;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while( (line = reader.readLine()) !=null )
            {
                vertexCode.append(line);
                vertexCode.append('\n');
            }
        }
        catch(Exception e)
        {
                throw new IllegalArgumentException("unable to load shader from file ["+filename+"]", e);
        }

        return vertexCode.toString();
    }
}
