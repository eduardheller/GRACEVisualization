/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLScene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import vhci.GL.GLMesh;
import vhci.GL.GLObject.Components.GLCameraComponent;
import vhci.GL.GLObject.Components.GLStaticMeshComponent;
import vhci.GL.GLObject.GLActor;
import vhci.GL.GLRenderer;
import vhci.GL.GLTimerAnimation;

/**
 *
 * @author grafiklabor
 */
public class GLExternalSceneAccess {
    private GLScene CurrentScene;
    private GLActor Planet;
    private GLActor SelectedVertexActor;
    private GLRenderer Renderer;
    private GLTimerAnimation TimerAnimation;
    
    public GLActor getSelectedVertexActor() {
        return SelectedVertexActor;
    }

    public void setSelectedVertexActor(GLActor SelectedVertexActor) {
        this.SelectedVertexActor = SelectedVertexActor;
    }
    
    public GLExternalSceneAccess(GLScene CurrentScene, GLActor Planet, GLRenderer Renderer, GLTimerAnimation TimerAnimation) {
        this.CurrentScene = CurrentScene;
        this.Planet = Planet;
        this.Renderer = Renderer;
        this.TimerAnimation = TimerAnimation;
    }

    public GLActor getPlanet() {
        return Planet;
    }
    
    public GLCameraComponent getActiveCamera(){
        return this.CurrentScene.getActiveCamera();
    }
    
    public void RotatePlanet( Vector3f Rotation ){         
        this.Planet.getRootComponent().addRelativeRotation(Rotation);
    }
    
    public Matrix4f getPlanetModelMatrix(){
        return this.Planet.getRootComponent().getFirstChild().getWorldTransform().getMatrix();
    }
    
    public GLCameraComponent GetCamera()
    {
        return CurrentScene.getActiveCamera();
    }
    
    public GLMesh GetModel()
    {
        return ((GLStaticMeshComponent)this.Planet.getRootComponent().getFirstChild()).getMesh();
    }
    
    public void ScalePlanet( Vector3f Scale ){
        this.Planet.getRootComponent().addRelativeScale(Scale);
    }
    
    public void setAnimationSpeed(float Value){        
        this.TimerAnimation.setSpeed(Value);
    }
    
    public float getAnimationSpeed(){        
        return this.TimerAnimation.getSpeed();
    }
    
    public Vector2f getMinMaxRange(){
        return new Vector2f(this.Renderer.getMinMaxRange());
    }
    
    public Vector2f getMinMaxColor(){
        return new Vector2f(this.Renderer.getMinMaxColor());
    }
    
    public void changeMinColor(float Value){
        this.Renderer.getMinMaxColor().x = Value;
    }
    
    public void changeMaxColor(float Value){
        this.Renderer.getMinMaxColor().y = Value;
    }
    
    public void changeMinRange(float Value){
        this.Renderer.getMinMaxRange().x = Value;
    }
    
    public void changeMaxRange(float Value){
        this.Renderer.getMinMaxRange().y = Value;
    }
    
    public void setColorFading(float Value){
        this.Renderer.setColorFade(Value);
    }
    
    public float getColorFading(){
        return this.Renderer.getColorFade();
    }
        
    public void changeDisplacement(float Value){
        
    }
}
