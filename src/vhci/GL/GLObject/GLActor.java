/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject;

import java.util.ArrayList;
import vhci.GL.GLObject.Components.GLComponent;
import vhci.GL.GLObject.Components.GLSceneComponent;
import vhci.GL.GLScene.GLScene;

/**
 *
 * @author grafiklabor
 */
public class GLActor {
    
    protected ArrayList<GLComponent> Components = new ArrayList<GLComponent>();
    protected GLSceneComponent RootComponent;
    protected GLScene OwningScene;
    
    
    public GLSceneComponent getRootComponent(){
        return this.RootComponent;        
    }
    
    public void setRootComponent(GLSceneComponent NewRootComponent){
        this.RootComponent = NewRootComponent;
        NewRootComponent.setOwner(this);
    }
    
    public void addComponent( GLComponent Child ){
        this.Components.add(Child);
        Child.setOwner(this);
    }
    
    public void setScene(GLScene OwningScene){
        this.OwningScene = OwningScene;
    }
    
    public GLScene getScene(){
        return this.OwningScene;
    }
}
