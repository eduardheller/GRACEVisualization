/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLScene;

import java.util.ArrayList;
import vhci.GL.GLObject.Components.GLCameraComponent;
import vhci.GL.GLObject.Components.GLDirectionalLightComponent;
import vhci.GL.GLObject.Components.GLSceneComponent;
import vhci.GL.GLObject.Components.GLShapeComponent;
import vhci.GL.GLObject.Components.GLStaticMeshComponent;
import vhci.GL.GLObject.GLActor;

/**
 *
 * @author grafiklabor
 */
public class GLScene {
    private ArrayList<GLActor> AllActors = new ArrayList<GLActor>();
    private ArrayList<GLSceneComponent> AllSceneComponents = new ArrayList<GLSceneComponent>();
    private ArrayList<GLStaticMeshComponent> AllStaticMeshComponent = new ArrayList<GLStaticMeshComponent>();
    private ArrayList<GLShapeComponent> AllShapeComponents = new ArrayList<GLShapeComponent>();
    
    
    private ArrayList<GLDirectionalLightComponent> AllLightComponents = new ArrayList<GLDirectionalLightComponent>();
    
    private GLCameraComponent ActiveCamera;
    
    public void SpawnActor(GLActor NewActor){
        if( !this.AllActors.contains(NewActor)){
            this.AllActors.add(NewActor);
            NewActor.setScene(this);
            NewActor.getRootComponent().RegisterComponent();
        }
    }
    
    public void registerSceneComponent( GLSceneComponent Component ){
        if( !this.AllSceneComponents.contains(Component)){
            this.AllSceneComponents.add(Component);
        }
        
        if(Component instanceof GLStaticMeshComponent){
            this.AllStaticMeshComponent.add((GLStaticMeshComponent)Component);
        }
        
        if(Component instanceof GLCameraComponent){
            this.ActiveCamera = (GLCameraComponent)Component;
        }
        
        if(Component instanceof GLDirectionalLightComponent){
            AllLightComponents.add((GLDirectionalLightComponent)Component);
        }
        
        if( Component.getClass().equals(GLShapeComponent.class) ){
            AllShapeComponents.add((GLShapeComponent)Component);
        }
    }
    
    public ArrayList<GLActor> getAllActors(){
        return this.AllActors;
    }
    
    public ArrayList<GLSceneComponent> getAllSceneComponents(){
        return this.AllSceneComponents;
    }
    
    public ArrayList<GLStaticMeshComponent> getAllStaticMeshComponents(){
        return this.AllStaticMeshComponent;
    }

    public ArrayList<GLDirectionalLightComponent> getAllLightComponents() {
        return AllLightComponents;
    }
    
    public GLCameraComponent getActiveCamera(){
        return this.ActiveCamera;
    }

    public ArrayList<GLShapeComponent> getAllShapeComponents() {
        return AllShapeComponents;
    }

    
    
}
