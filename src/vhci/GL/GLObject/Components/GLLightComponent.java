/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import org.joml.Vector3f;
import vhci.GL.GLScene.GLScene;

/**
 *
 * @author Phr34z3r
 */
public class GLLightComponent extends GLSceneComponent {
    private float AmbientIntensity;
    private float DiffuseIntensity;
    private Vector3f LightColor;

    public float getAmbientIntensity() {
        return AmbientIntensity;
    }

    public void setAmbientIntensity(float AmbientIntensity) {
        this.AmbientIntensity = AmbientIntensity;
    }

    public float getDiffuseIntensity() {
        return DiffuseIntensity;
    }

    public void setDiffuseIntensity(float DiffuseIntensity) {
        this.DiffuseIntensity = DiffuseIntensity;
    }

    public Vector3f getLightColor() {
        return LightColor;
    }

    public void setLightColor(Vector3f LightColor) {
        this.LightColor = LightColor;
    }
}
