/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import vhci.GL.GLMesh;

/**
 *
 * @author Phr34z3r
 */
public class GLShapeComponent extends GLSceneComponent{
    protected GLMesh Mesh;
    
    public void setMesh( GLMesh NewModel ){
        this.Mesh = NewModel;
    }

    public GLMesh getMesh() {
        return Mesh;
    }
}
