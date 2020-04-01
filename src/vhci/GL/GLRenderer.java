/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;


import vhci.GL.GLObject.Components.GLCameraComponent;
import com.sun.javafx.geom.Vec2f;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import vhci.GL.GLObject.Components.GLDirectionalLightComponent;
import vhci.GL.GLObject.Components.GLShapeComponent;
import vhci.GL.GLObject.Components.GLStaticMeshComponent;
import vhci.GL.GLScene.GLScene;
import vhci.GRACE.GRACEFileLoader;
import vhci.GRACE.GRACELoader;

/**
 *
 * @author rohdej
 */
public class GLRenderer{
    private GLShaderProgram StandardShader;
    private GLShaderProgram ShapeShader;
    
    private GLCameraComponent ActiveCamera;

    private boolean RenderPolyLine = false;
    
    private Vector2f MinMaxColor;
    private Vector2f MinMaxRange;
    
    private float ColorFade = 0.0f;
    
    public GLRenderer() {
        StandardShader = new GLShaderProgram();
        StandardShader.loadShader(  "./src/vhci/GL/Shaders/StandardVertexShader.vs",
                                    "./src/vhci/GL/Shaders/StandardFragmentShader.fs");   
        StandardShader.createUniform("AlbedoMap");
        StandardShader.createUniform("Timer");
        StandardShader.createUniform("MVP");
        StandardShader.createUniform("M");
        StandardShader.createUniform("V");
        StandardShader.createUniform("P");
        StandardShader.createUniform("hasScaling");
        StandardShader.createUniform("minRange");
        StandardShader.createUniform("maxRange");
        StandardShader.createUniform("minColor");
        StandardShader.createUniform("maxColor");
        
        StandardShader.createUniform("ColorFade");
        
        StandardShader.createUniform("CameraWorldLocation");
        StandardShader.createUniform("LightDirection");
        StandardShader.createUniform("LightColor");
        StandardShader.createUniform("LightAmbientIntensity");
        StandardShader.createUniform("LightDiffuseIntensity");
        
        ShapeShader = new GLShaderProgram();
        ShapeShader.loadShader( "./src/vhci/GL/Shaders/MousePickerVertexShader.vs",
                                "./src/vhci/GL/Shaders/MousePickerFragmentShader.fs");
        
        ShapeShader.createUniform("MVP");
        ShapeShader.createUniform("M");
        
        MinMaxRange = new Vector2f(-8.0f, 20.0f);
        MinMaxColor = new Vector2f(0.0f, 240.0f);
    }
    
    public void RenderScene(GLScene CurrentScene, GRACEFileLoader loader, GLTimerAnimation timerAnimation, double deltaTime){
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE); // cull face
        glCullFace(GL_BACK); // cull back face
        glFrontFace(GL_CW); // GL_CCW for counter clock-wise
        
            
        this.ActiveCamera = CurrentScene.getActiveCamera();
        
        GLDirectionalLightComponent Sun = CurrentScene.getAllLightComponents().get(0);
        
        Matrix4f VP = new Matrix4f();
        VP.mul(this.ActiveCamera.getProjectionMatrix()).mul(this.ActiveCamera.getViewMatrix());
        
        glPointSize(4);
        
        if(RenderPolyLine) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        RenderGeometry(VP, Sun, loader, CurrentScene.getAllStaticMeshComponents(), timerAnimation, deltaTime);
        if(RenderPolyLine) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
       if(RenderPolyLine) RenderShapes(VP, CurrentScene.getAllShapeComponents(), deltaTime);
    }
    
    private void RenderGeometry( Matrix4f VP, 
            GLDirectionalLightComponent Sun, GRACEFileLoader loader,
            ArrayList<GLStaticMeshComponent> Components, GLTimerAnimation timerAnimation,double deltaTime){

        StandardShader.startShader(); 
        
        for( GLStaticMeshComponent Component : Components){
            Matrix4f ModelMatrix = Component.getWorldTransform().getMatrix();
            GLMesh Mesh = Component.getMesh();
           
            Matrix4f MVP = new Matrix4f();
            MVP.mul(new Matrix4f(VP)).mul(ModelMatrix);
    
            StandardShader.setUniform("minRange", MinMaxRange.x);
            StandardShader.setUniform("maxRange", MinMaxRange.y);
            StandardShader.setUniform("minColor", MinMaxColor.x);
            StandardShader.setUniform("maxColor", MinMaxColor.y);
            StandardShader.setUniform("ColorFade", ColorFade);
            
            StandardShader.setUniform("MVP", MVP);
            StandardShader.setUniform("M", ModelMatrix);
            StandardShader.setUniform("Timer", timerAnimation.getTimer());

            StandardShader.setUniform("CameraWorldLocation", this.ActiveCamera.getWorldTransform().getLocation());
            
            StandardShader.setUniform("LightDirection", Sun.getLightDirection());
            StandardShader.setUniform("LightColor", Sun.getLightColor());
            StandardShader.setUniform("LightAmbientIntensity", Sun.getAmbientIntensity());
            StandardShader.setUniform("LightDiffuseIntensity", Sun.getDiffuseIntensity());
            
            glEnable(GL_TEXTURE_2D);
            glActiveTexture(0);
            glBindTexture(GL_TEXTURE_2D, Component.getTexture().getTextureID());
            StandardShader.setUniform("AlbedoMap", 0);
            
            
            // Draw the mesh
            glBindVertexArray(Mesh.getVaoId());
            glDrawElements(GL_TRIANGLES,Mesh.getIndicesCount(), GL_UNSIGNED_INT, NULL);
            //glDrawArrays(GL_POINTS,0,Mesh.getVertexCount());
            glBindVertexArray(0);
            
            if(Mesh.changeData) Mesh.init();
            timerAnimation.refreshTimer((float)deltaTime);
            timerAnimation.update(loader,Mesh);
          
        }
    }
    
    private void RenderShapes(Matrix4f VP, ArrayList<GLShapeComponent> Components, double deltaTime){
        ShapeShader.startShader(); 
        
        for( GLShapeComponent Component : Components){
            Matrix4f ModelMatrix = Component.getWorldTransform().getMatrix();
            GLMesh Mesh = Component.getMesh();
           
            Matrix4f MVP = new Matrix4f();
            MVP.mul(new Matrix4f(VP)).mul(ModelMatrix);
            ShapeShader.setUniform("MVP", MVP);
            ShapeShader.setUniform("M", ModelMatrix);
            
            glBindVertexArray(Mesh.getVaoId());
            glDrawArrays(GL_TRIANGLES,0,Mesh.getVertexCount());
            glBindVertexArray(0);
        }
    }

    public void setRenderPolyLine(boolean RenderPolyLine) {
        this.RenderPolyLine = RenderPolyLine;
    }

    public Vector2f getMinMaxColor() {
        return MinMaxColor;
    }

    public Vector2f getMinMaxRange() {
        return MinMaxRange;
    }

    public void setColorFade(float ColorFade) {
        this.ColorFade = ColorFade;
    }

    public float getColorFade() {
        return ColorFade;
    }

    
    
}
