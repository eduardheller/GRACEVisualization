/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author Phr34z3r
 */
public class GLSphereMesh extends GLMesh {
    
    @Override
    protected void initMeshData() {
        initSphere();
    }
    
    
    public void initSphere(){        
	double PI = 3.14159;
	float Radius = 1;
	int Resolution = 20;

        float X1, Y1, X2, Y2, Z1, Z2;
	float inc1, inc2, inc3, inc4, inc5, Radius1, Radius2;

	for (int w = 0; w < Resolution; w++) {
            for (int h = (-Resolution / 2); h < (Resolution / 2); h++) {

                inc1 = (w / (float)Resolution) * 2 * (float)PI;
                inc2 = ((w + 1) / (float)Resolution) * 2 * (float)PI;

                inc3 = (h / (float)Resolution)*(float)PI;
                inc4 = ((h + 1) / (float)Resolution)*(float)PI;

                X1 = (float)Math.sin(inc1);
                Y1 = (float)Math.cos(inc1);
                X2 = (float)Math.sin(inc2);
                Y2 = (float)Math.cos(inc2);

                // store the upper and lower radius, remember everything is going to be drawn as triangles
                Radius1 = Radius*(float)Math.cos(inc3);
                Radius2 = Radius*(float)Math.cos(inc4);

                Z1 = Radius*(float)Math.sin(inc3);
                Z2 = Radius*(float)Math.sin(inc4);

                // insert the triangle coordinates
                vertices.add(new Vector3f(Radius1*X1, Z1, Radius1*Y1));
                vertices.add(new Vector3f(Radius1*X2, Z1, Radius1*Y2));
                vertices.add(new Vector3f(Radius2*X2, Z2, Radius2*Y2));

                vertices.add(new Vector3f(Radius1*X1, Z1, Radius1*Y1));
                vertices.add(new Vector3f(Radius2*X2, Z2, Radius2*Y2));
                vertices.add(new Vector3f(Radius2*X1, Z2, Radius2*Y1));

                uvs.add(new Vector2f(0.6666f, 0.5f).mul(new Vector2f(1, 0.25f)).add(new Vector2f(0, 0.25f)));
                uvs.add(new Vector2f(0.666f, 1.0f).mul(new Vector2f(1, 0.25f)).add(new Vector2f(0, 0.25f)));
                uvs.add(new Vector2f(1.0f, 1.0f).mul(new Vector2f(1, 0.25f)).add(new Vector2f(0, 0.25f)));

                uvs.add(new Vector2f(0.6666f, 0.5f).mul( new Vector2f(1, 0.25f) ).add(new Vector2f(0, 0.25f)));
                uvs.add(new Vector2f(1.00f, 1.0f).mul(new Vector2f(1, 0.25f) ).add(new Vector2f(0, 0.25f)));
                uvs.add(new Vector2f(1.00f, 0.5f).mul(new Vector2f(1, 0.25f) ).add( new Vector2f(0, 0.25f)));

                normals.add(new Vector3f(X1, Z1, Y1).div(new Vector3f(X1, Z1, Y1).length()));
                normals.add(new Vector3f(X2, Z1, Y2).div(new Vector3f(X2, Z1, Y2).length()));
                normals.add(new Vector3f(X2, Z2, Y2).div(new Vector3f(X2, Z2, Y2).length()));
                normals.add(new Vector3f(X1, Z1, Y1).div(new Vector3f(X1, Z1, Y1).length()));
                normals.add(new Vector3f(X2, Z2, Y2).div(new Vector3f(X2, Z2, Y2).length()));
                normals.add(new Vector3f(X1, Z2, Y1).div(new Vector3f(X1, Z2, Y1).length()));
            }
	}        
    } 

}
