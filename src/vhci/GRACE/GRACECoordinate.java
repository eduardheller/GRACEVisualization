/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci.GRACE;

/**
 *
 * @author Eduard
 */
public class GRACECoordinate {
        public int longtitude; 
        public int latitude;  

        public GRACECoordinate(int longt, int lat)
        {
            longtitude = longt;
            latitude = lat;
        }
        
        @Override
        public boolean equals(Object other){
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof GRACECoordinate)) return false;
            GRACECoordinate otherMyClass = (GRACECoordinate)other;
            return otherMyClass.latitude == this.latitude;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + this.longtitude;
            hash = 17 * hash + this.latitude;
            return hash;
        }
}
