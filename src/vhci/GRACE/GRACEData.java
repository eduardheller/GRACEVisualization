/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GRACE;

import java.util.HashMap;

/**
 *
 * @author Eduard
 */
public class GRACEData {
    
    public HashMap<GRACECoordinate,Float> data;
    public String date;
    public GRACEData(String date, HashMap<GRACECoordinate,Float> dat)
    {
        this.date = date;
        this.data = dat;
    }
}
