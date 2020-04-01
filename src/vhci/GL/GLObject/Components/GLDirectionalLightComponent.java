/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import org.joml.Vector3f;

/**
 *
 * @author Phr34z3r
 */
public class GLDirectionalLightComponent extends GLLightComponent {
    private Vector3f LightDirection;

    public GLDirectionalLightComponent() {
        this.setAmbientIntensity(0.35f);
	this.setDiffuseIntensity(0.5f);
        this.setLightColor(new Vector3f(1.0f, 1.0f, 1.0f));
	this.LightDirection = new Vector3f(-.5f, -.5f, -1.f);
    }

    public Vector3f getLightDirection() {
        return LightDirection;
    }

    public void setLightDirection(Vector3f LightDirection) {
        this.LightDirection = LightDirection;
    }
    
    
    
}
