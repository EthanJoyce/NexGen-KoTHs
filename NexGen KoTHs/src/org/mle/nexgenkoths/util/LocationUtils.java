package org.mle.nexgenkoths.util;

import org.bukkit.Location;
import org.mle.nexgenkoths.LocationPair;

public enum LocationUtils {;
    
    public static boolean isLocationInside(Location loc, LocationPair pair) {
        Location top, bottom;
        int topX, topY, topZ, botX, botY, botZ, x, y, z;
        
        if(pair.getLocation1().getY() > pair.getLocation2().getY()) {
            top = pair.getLocation1();
            bottom = pair.getLocation2();
        } else {
            bottom = pair.getLocation1();
            top = pair.getLocation2();
        }
        
        topX = top.getBlockX();
        topY = top.getBlockY();
        topZ = top.getBlockZ();
        
        botX = bottom.getBlockX();
        botY = bottom.getBlockY();
        botZ = bottom.getBlockZ();
        
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
        
        
        if(botX > topX) { // Bottom X is largest
            if(topZ > botZ) { // Top Z is largest

                if(x >= topX && x <= botX) {
                    if(y >= botY && y <= topY) {
                        if(z >= botZ && z <= topZ) {
                            return true;
                        }
                    }
                }
            }
            else if(botZ > topZ) { // Bottom Z is largest

                if(x >= topX && x <= botX) {
                    if(y >= botY && y <= topY) {
                        if(z >= topZ && z <= botZ) {
                            return true;
                        }
                    }
                }
            }
        }
        else if(topX > botX) { // Top X is largest
            if(topZ > botZ) { // Top Z is largest
                
                if(x >= botX && x <= topX) {
                    if(y >= botY && y <= topY) {
                        if(z >= botZ && z <= topZ) {
                            return true;
                        }
                    }
                }
            }
            else if(botZ > topZ) { // Bottom Z is largest
                
                if(x >= botX && x <= topX) {
                    if(y >= botY && y <= topY) {
                        if(z >= topZ && z <= botZ) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    
}
