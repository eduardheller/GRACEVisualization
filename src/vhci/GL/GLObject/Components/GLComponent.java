/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import vhci.GL.GLObject.GLActor;

/**
 *
 * @author grafiklabor
 */
public class GLComponent {
    
    protected GLActor Owner;
    
    public void setOwner( GLActor NewOwner){
        this.Owner = NewOwner;
    }
    
    public GLActor getOwner(){
        return Owner;
    }
    
    public void RegisterComponent()
    {
    }
}
