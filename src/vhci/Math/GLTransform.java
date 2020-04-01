/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.Math;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 *
 * @author rohdej
 */
public class GLTransform {
    Vector3f Location;
    Vector3f Rotation;
    Vector3f Scale;

    public GLTransform() {
        Location = new Vector3f(0,0,0);
        Rotation = new Vector3f(0,0,0);
        Scale = new Vector3f(1,1,1);
    }
    
    public Matrix4f getMatrix(){
       Matrix4f m = new Matrix4f();  m.identity();
       
       Matrix4f rotationMatrix = new Matrix4f();
       
       Quaternionf Quat = new Quaternionf().rotationXYZ(Rotation.x, Rotation.y, Rotation.z);
       
       Quat.get(rotationMatrix);
       Matrix4f scalingMatrix = new Matrix4f();
       scalingMatrix.scale(Scale);
       
       Matrix4f translationMatrix = new Matrix4f();
       translationMatrix.translate(Location);

       m.mul(translationMatrix)
        .mul(rotationMatrix)
        .mul(scalingMatrix);

       return m;
    }
    
    public void setMatrix(Matrix4f NewMatrix){
        Quaternionf Quat = new Quaternionf();
        NewMatrix.getNormalizedRotation(Quat);
        //Quat = Quat.conjugate();
        Quat.getEulerAnglesXYZ(this.Rotation);
        
        //NewMatrix.getEulerAnglesZYX(this.Rotation);
        NewMatrix.getTranslation(this.Location);
        NewMatrix.getScale(this.Scale);
    }
    
    public void setLocation(Vector3f NewLocation){
        this.Location = NewLocation;
    }
    
    public void addLocation(Vector3f NewLocation){
        this.Location.add(NewLocation);
    }
    
    public Vector3f getLocation(){
        return new Vector3f(this.Location);
    }
    
    public void setRotation(Vector3f NewRotation){
        this.Rotation = NewRotation;
    }
    
    public void addRotation(Vector3f NewRotation){
        this.Rotation.add(NewRotation);
    }
    
    public Vector3f getRotation(){
        return new Vector3f(this.Rotation);
    }
    
    public void setScale(Vector3f NewScale){
        this.Scale = NewScale;
    }
    
    public void addScale(Vector3f NewScale){
        this.Scale.add(NewScale);
    }
    
    public Vector3f getScale(){
        return new Vector3f(this.Scale);
    }
    
    public Vector3f getRightVector(){
        Matrix4f Mat = this.getMatrix();
        Vector4f Column = new Vector4f();        
        Mat.getColumn(0, Column);
        return new Vector3f(Column.x, Column.y, Column.z);
    }
    
    public Vector3f getUpVector(){        
        Matrix4f Mat = this.getMatrix();
        Vector4f Column = new Vector4f();
        Mat.getColumn(1, Column);
        return new Vector3f(Column.x, Column.y, Column.z);        
    }
    
    public Vector3f getForwardVector(){
        Matrix4f Mat = this.getMatrix();
        Vector4f Column = new Vector4f();
        Mat.getColumn(2, Column);
        return new Vector3f(Column.x, Column.y, Column.z);
    }
    
    


    
    
}
