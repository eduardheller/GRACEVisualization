/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import vhci.GL.GLMesh;
import vhci.GL.GLTexture;

/**
 *
 * @author grafiklabor
 */
    public class GLStaticMeshComponent extends GLShapeComponent{
        
    protected GLTexture Texture;
    public GLTexture getTexture() {
        return Texture;
    }

    public void setTexture(GLTexture Texture) {
        this.Texture = Texture;
    }
    
    
}
