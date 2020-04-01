/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL.MousePicker;

import javafx.beans.binding.Bindings;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import vhci.GL.GLObject.Components.GLCameraComponent;
import vhci.GL.GLObject.Components.GLStaticMeshComponent;
import vhci.GRACE.GRACECoordinate;

/**
 *
 * @author Phr34z3r
 */
public class MousePicker {
    
    private float MaxDistanceToVertex = 100;
    
    public GRACECoordinate pickVertex( GLStaticMeshComponent Planet, GLCameraComponent Camera, Vector2f WindowSize, Vector2f MousePosition){
        
        MousePickerRay Ray = CastRay( Camera, WindowSize, MousePosition );
        
        
        
        return null;
    }
    
    public MousePickerRay CastRay( GLCameraComponent Camera, Vector2f WindowSize, Vector2f MousePosition){
        
        Vector4f RayStart_SS = new Vector4f( 
                ((float)MousePosition.x / (float)WindowSize.x - 0.5f) * 2.0f, // [-1,1]
                -((float)MousePosition.y / (float)WindowSize.y - 0.5f) * 2.0f,
                -1.0f, // Z=-1 near plane
                1.0f);
        
        Vector4f RayEnd_SS = new Vector4f(
                ((float)MousePosition.x / (float)WindowSize.x - 0.5f) * 2.0f, // [-1,1]
                -((float)MousePosition.y / (float)WindowSize.y - 0.5f) * 2.0f,
                0.0f, // Z=0 far plane
                1.0f);

        //System.out.printf("dexp: %f  %f  %f\n", RayStart_SS.x, RayStart_SS.y, RayStart_SS.z);
        //System.out.printf("dexp: %f  %f  %f\n", RayEnd_SS.x, RayEnd_SS.y, RayEnd_SS.z);
        
        Matrix4f InverseViewProjectionMatrix = Camera.getProjectionMatrix().mul(Camera.getViewMatrix());
        InverseViewProjectionMatrix = InverseViewProjectionMatrix.invert();
        
        Vector4f RayStart_World = new Matrix4f( InverseViewProjectionMatrix ).transform(new Vector4f(RayStart_SS));
        RayStart_World = RayStart_World.div(RayStart_World.w);
        
        Vector4f RayEnd_World = new Matrix4f( InverseViewProjectionMatrix ).transform(new Vector4f(RayEnd_SS));
        RayEnd_World = RayEnd_World.div(RayEnd_World.w);
        
        Vector4f RayDirection = RayEnd_World.sub(RayStart_World);
        RayDirection = RayDirection.normalize();
        
        MousePickerRay Ray = new MousePickerRay();
        
        Ray.setRayStart( Camera.getWorldTransform().getLocation() );
        Ray.setRayEnd(Camera.getWorldTransform().getLocation().sub(new Vector3f(RayDirection.x, RayDirection.y, RayDirection.z).mul(10.0f)));
        Ray.setRayDirection(new Vector3f(RayDirection.x, RayDirection.y, RayDirection.z));
        
        
        //System.out.printf("dexp: %f  %f  %f     ", Ray.getRayStart().x, Ray.getRayStart().y, Ray.getRayStart().z);
        //System.out.printf("dexp: %f  %f  %f\n", Ray.getRayEnd().x, Ray.getRayEnd().y, Ray.getRayEnd().z);
        
        return Ray;
    }
    
    public float CalculateDistanceToPoint( MousePickerRay Ray, Vector3f P0 ){        
        Vector3f P1 = new Vector3f(Ray.getRayStart()) ;
        Vector3f P2 = new Vector3f(Ray.getRayEnd());
        
        return ((new Vector3f(P1).sub(P0)).cross(new Vector3f(P1).sub(P2))).length()  / ((new Vector3f(P1).sub(P2)).length());
    }
    
}
