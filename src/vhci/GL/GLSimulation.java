/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;

import vhci.GL.GLObject.Components.GLCameraComponent;
import com.sun.javafx.geom.Vec2f;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import org.joml.Vector2f;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import vhci.GL.GLObject.Components.GLStaticMeshComponent;
import vhci.GL.GLScene.GLScene;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import static org.lwjgl.opengl.GL11.glViewport;
import vhci.GL.GLObject.GLActor;
import vhci.GL.GLScene.GLExternalSceneAccess;
import vhci.GRACE.GRACELoader;
import vhci.GL.GLObject.Components.*;
import vhci.GRACE.GRACEFileLoader;

/**
 *
 * @author rohdej
 */
public final class GLSimulation{
    private Vector2f PanelSize;
    
    private GLScene CurrentScene;
    private GLRenderer Renderer;

    public GRACEFileLoader GraceLoader;
    public GLTimerAnimation TimerAnimation;
    GLExternalSceneAccess SceneAccess;
    GLFWWindowSizeCallback windowSizeCallback;
    
    long last_time;
    double elapsedTimeInSeconds;
    
    boolean updateViewport = false;
    
    public GLSimulation() {
        
    }
    
    public GLExternalSceneAccess getSceneAccess(){
        return this.SceneAccess;
    }
        
    public void Initialize(GRACEFileLoader grace, Vector2f PanelSize) {
        GL.createCapabilities();
        glViewport(0, 0, (int)PanelSize.x, (int)PanelSize.y);
        this.PanelSize = new Vector2f( PanelSize );
        
        Renderer = new GLRenderer();
        
        // Init Starting Scene
        this.CurrentScene = new GLScene();
        // Init Camera
        GLActor ActorCamera = new GLActor();
        
        GLCameraComponent Camera = new GLCameraComponent();
        Camera.setProjectionMatrix((float) Math.toRadians(45.0f), PanelSize.x/PanelSize.y, 0.01f, 1000.0f);
        Camera.setRelativeLocation(new Vector3f(0,0,3));
        Camera.setRelativeRotation(new Vector3f(0,0,0));
        
        GLSceneComponent RootComponent = new GLSceneComponent();
        RootComponent.setRelativeLocation(new Vector3f(0.0f, 0.0f, 0.0f));
        RootComponent.addChildComponent(Camera);
        
        ActorCamera.setRootComponent(RootComponent);
        this.CurrentScene.SpawnActor(ActorCamera);
       
        GLActor Actor = new GLActor();
        GLStaticMeshComponent MeshComponent= new GLStaticMeshComponent();
        
        GraceLoader = grace;
        TimerAnimation = new GLTimerAnimation();
        double StartTime = System.currentTimeMillis();
        
        GLMesh Mesh = new GLEarthMesh(GraceLoader,TimerAnimation);
        
        Mesh.init();
        
        GLTexture Texture = new GLTexture();
        Texture.loadTexture("Extern/Resources/worldmap.jpg");
        
        MeshComponent.setTexture(Texture);
        MeshComponent.setMesh(Mesh);
        MeshComponent.getRelativeTransform().setLocation(new Vector3f(0.0f, 0.0f, 0f));        
        MeshComponent.getRelativeTransform().setRotation(new Vector3f(0, 0, 0)); 
        
        GLSceneComponent RootComponent2 = new GLSceneComponent();
        RootComponent2.setRelativeLocation(new Vector3f(0.0f, 0.0f, 0.0f));
        RootComponent2.addChildComponent(MeshComponent);
        
        Actor.setRootComponent(RootComponent2);
        this.CurrentScene.SpawnActor(Actor);
       
        this.SceneAccess = new GLExternalSceneAccess(CurrentScene, Actor, Renderer, TimerAnimation);
        
        GLDirectionalLightComponent Sun = new GLDirectionalLightComponent();        
        Actor = new GLActor();
        Actor.setRootComponent(Sun);
        this.CurrentScene.SpawnActor(Actor);  
        
        GLSphereMesh Sphere = new GLSphereMesh();  
        Sphere.init();

        GLShapeComponent ShapeComponent = new GLShapeComponent();
        ShapeComponent.setMesh(Sphere);
        ShapeComponent.setRelativeScale(new Vector3f(0.01f, 0.01f, 0.01f));
        
        Actor = new GLActor();
        Actor.setRootComponent(ShapeComponent);
        this.CurrentScene.SpawnActor(Actor);
        this.SceneAccess.setSelectedVertexActor(Actor);
        
        last_time = System.nanoTime();
    }
    
    public void run() {
        long time = System.nanoTime();
        long delta_time = time - last_time;
        elapsedTimeInSeconds = TimeUnit.MILLISECONDS.convert(delta_time, TimeUnit.NANOSECONDS) / 1000.0;
        last_time = time;
        
        if( updateViewport ) updateViewport();
        Renderer.RenderScene(this.CurrentScene,GraceLoader,TimerAnimation,elapsedTimeInSeconds);        
    }
    
    

    public float getDeltaTime()
    {
        return (float)elapsedTimeInSeconds;
    }

    public void setPanelSize( int Width, int Height ){
        PanelSize = new Vector2f(Width, Height);
        this.CurrentScene.getActiveCamera().setProjectionMatrix( (float) Math.toRadians(45.0f), (float)Width/(float)Height, 0.01f, 1000.0f);        
        updateViewport = true;
    }

    public Vector2f getPanelSize() {
        return new Vector2f(PanelSize);
    }

    public void setRenderPolyline(boolean Value){
        this.Renderer.setRenderPolyLine(Value);
    }
    
    private void updateViewport(){
        glViewport(0, 0, (int)PanelSize.x, (int)PanelSize.y);
        updateViewport = false;
    }
}
