/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import vhci.GRACE.GRACEFileLoader;

/**
 *
 * @author Eduard
 */
public class GLTimerAnimation {
    
    public float lerpTimer = 0.0f;
    public boolean shouldPlay = false;
    public boolean lerp = false;
    private List listeners = new ArrayList();
    private float speed = 1.0f;
    
    public synchronized void addEventListener(GLEventClassListener listener)  {
        listeners.add(listener);
    }
    
    public void refreshTimer(float delta)
    {
        lerpTimer += delta * speed;
        lerpTimer = GLMathHelper.clamp(lerpTimer, 0, 1);
    }
    
    public void setSpeed(float spd)
    {
        speed = spd;
    }
    
    public float getSpeed()
    {
        return speed;
    }
    
    public void setNoLerp()
    {
        lerp = false;
        shouldPlay  = false;
        lerpTimer = 1;
    }
    
    public void setDefaultTimer()
    {
        lerpTimer = 0;
    }
    
    public float getTimer()
    {
        return lerpTimer;
    }

    public void update(GRACEFileLoader grace, GLMesh mesh)
    {
        
        if(shouldPlay)
        {
            lerp = true;
            if(lerpTimer>=1)
            {
                if(!grace.next()) shouldPlay = false;
                mesh.changeData = true;
                changedMesh();
            }
        }
    }
    
    public void changedMesh()
    {
        GLEvent event = new GLEvent(this);
        Iterator i = listeners.iterator();
        while(i.hasNext())
        {
            ((GLEventClassListener) i.next()).handleEvent(event);
        }
        
    }

}
