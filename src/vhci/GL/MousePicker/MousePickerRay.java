/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.MousePicker;

import org.joml.Vector3f;

/**
 *
 * @author Phr34z3r
 */
public class MousePickerRay {
    private Vector3f RayStart;
    private Vector3f RayEnd;
    private Vector3f RayDirection;

    public Vector3f getRayStart() {
        return new Vector3f(RayStart);
    }

    public void setRayStart(Vector3f RayStart) {
        this.RayStart = RayStart;
    }

    public Vector3f getRayEnd() {
        return new Vector3f(RayEnd);
    }

    public void setRayEnd(Vector3f RayEnd) {
        this.RayEnd = RayEnd;
    }

    public Vector3f getRayDirection() {
        return new Vector3f(RayDirection);
    }

    public void setRayDirection(Vector3f RayDirection) {
        this.RayDirection = RayDirection;
    }
    
    
}
