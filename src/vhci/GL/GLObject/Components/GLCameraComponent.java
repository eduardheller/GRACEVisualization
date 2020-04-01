/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author rohdej
 */
public class GLCameraComponent extends GLSceneComponent {
    private Matrix4f ProjectionMatrix = new Matrix4f();
    
    private float FOV;
    private float AspectRatio;
    private float NearClipping;
    private float FarClipping;
    
    public GLCameraComponent(){    
    }
    
    public void setProjectionMatrix(float FOV, float AspectRatio, float NearClipping, float FarClipping){
        this.FOV = FOV;
        this.AspectRatio = AspectRatio;
        this.NearClipping = NearClipping;
        this.FarClipping = FarClipping;
        
        this.ProjectionMatrix = new Matrix4f().perspective(FOV, AspectRatio, NearClipping, FarClipping); 
    }
    
    public Matrix4f getViewMatrix() {
       /* Matrix4f m = new Matrix4f();  m.identity();
        Matrix4f rotationMatrix = new Matrix4f();

        Quaternionf Quat = new Quaternionf().rotationXYZ(this.getWorldTransform().getRotation().x, this.getWorldTransform().getRotation().y, this.getWorldTransform().getRotation().z);
        Quat.get(rotationMatrix);

        Matrix4f translationMatrix = new Matrix4f();
        translationMatrix.translate(this.getWorldTransform().getLocation());

        (m.mul(rotationMatrix)
         .mul(translationMatrix))
         .invert();
        return m;
        */
       return this.getWorldTransform().getMatrix().invert();
    }
    
    public Matrix4f getProjectionMatrix() {        
        return new Matrix4f(this.ProjectionMatrix);
    }
}
