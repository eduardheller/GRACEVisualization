/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GL;

/**
 *
 * @author Eduard
 */
public class GLMathHelper {
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    

}
