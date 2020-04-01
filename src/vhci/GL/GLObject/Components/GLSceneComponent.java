/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.GLObject.Components;

import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vhci.GL.GLMesh;
import vhci.Math.GLTransform;

/**
 *
 * @author grafiklabor
 */
public class GLSceneComponent extends GLComponent {
    
    protected ArrayList<GLSceneComponent> ChildComponents = new ArrayList<GLSceneComponent>();
    protected GLTransform RelativeTransform;
    protected GLTransform WorldTransform;
    protected GLSceneComponent Parent;

    public GLSceneComponent() {
        RelativeTransform = new GLTransform();
        WorldTransform = new GLTransform();
    }

    private GLTransform CalculateWorldTransform(GLTransform RelativeTransform)
    {
        if( this.Parent != null)
        {
            GLTransform NewTransform = new GLTransform();
            NewTransform.setMatrix( this.Parent.getWorldTransform().getMatrix().mul(RelativeTransform.getMatrix()));
            
            return NewTransform;
        }
        else
        {
            return RelativeTransform;
        }
    }
    
    public void UpdateWorldTransform()
    {	
	this.WorldTransform = this.CalculateWorldTransform(this.RelativeTransform);
	
	for (GLSceneComponent Child : this.ChildComponents)
	{
            Child.UpdateWorldTransform();
        }
    }
   
    public void setRelativeTransform(Matrix4f mat)
    {
        this.RelativeTransform.setMatrix(mat);
        UpdateWorldTransform();
    }
    
    public GLSceneComponent getParent()
    {
        return Parent;
    }
    
    public GLTransform getRelativeTransform(){
        return this.RelativeTransform;
    }
    
    public GLTransform getWorldTransform(){
        return this.WorldTransform;
    }
    
    public void setRelativeLocation(Vector3f NewLocation){
        this.RelativeTransform.setLocation(NewLocation);
        UpdateWorldTransform();
    }
    
    public void addRelativeLocation(Vector3f NewLocation){
        this.RelativeTransform.addLocation(NewLocation);
        UpdateWorldTransform();
    }
    
    public Vector3f getRelativeLocation(){
        return this.RelativeTransform.getLocation();
    }
    
    public void setRelativeRotation(Vector3f NewRotation){
        this.RelativeTransform.setRotation(NewRotation);
        UpdateWorldTransform();
    }
    
    public void addRelativeRotation(Vector3f NewRotation){
        this.RelativeTransform.addRotation(NewRotation);
        UpdateWorldTransform();
    }
    
    public void addWorldRotation(Vector3f NewRotation){
       Vector3f OldRotation = new Vector3f(this.RelativeTransform.getRotation());
       Quaternionf QuatOld = new Quaternionf().rotationXYZ(OldRotation.x, OldRotation.y, OldRotation.z);
       Quaternionf QuatNew = new Quaternionf().rotationXYZ(NewRotation.x, NewRotation.y, NewRotation.z);
       
       Quaternionf ResultQuat = new Quaternionf();
       
       Matrix4f rotationMatrixNew = new Matrix4f();
       QuatNew.get(rotationMatrixNew);
       
       Matrix4f rotationMatrixOld = new Matrix4f();
       QuatOld.get(rotationMatrixOld);
       
       rotationMatrixOld.mul(rotationMatrixNew);
       
       rotationMatrixOld.getNormalizedRotation(ResultQuat);
       //ResultQuat = ResultQuat.conjugate();
       
       Vector3f ResultEuler = new Vector3f();
       ResultQuat.getEulerAnglesXYZ(ResultEuler);
       
       
       this.setRelativeRotation(ResultEuler);
    }
    
    public Vector3f getRelativeRotation(){
        return this.RelativeTransform.getRotation();
    }
    
    public void setRelativeScale(Vector3f NewScale){
        this.RelativeTransform.setScale(NewScale);
        UpdateWorldTransform();
    }
    
    public void addRelativeScale(Vector3f NewScale){
        this.RelativeTransform.addScale(NewScale);
        UpdateWorldTransform();
    }
    
    public Vector3f getRelativeScale(){
        return this.RelativeTransform.getScale();
    }
    
    public void addChildComponent( GLSceneComponent Child ){
        this.ChildComponents.add(Child);
        Child.Parent = this;
        Child.UpdateWorldTransform();
    }

    public GLSceneComponent getFirstChild()
    {
        if(this.ChildComponents.size()>0) return this.ChildComponents.get(0);
        else return null;
    }
    
    @Override
    public void RegisterComponent(){
        super.RegisterComponent();
        
        if(this.Parent != null){
            this.Owner = this.Parent.Owner;
        }
        
        if( this.Owner != null && this.Owner.getScene() != null ){
            this.Owner.getScene().registerSceneComponent(this);
        }
        
        for (GLSceneComponent Component : this.ChildComponents) {
            Component.RegisterComponent();
  }
    }
}
