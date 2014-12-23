package com.mrlolethan.nexgenkoths;

import org.bukkit.Location;

public class LocationPair {
    
	private Location loc1, loc2;
    
    public LocationPair(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
    
    
    public void setLocation1(Location loc1) {
        this.loc1 = loc1;
    }
    
    public void setLocation2(Location loc2) {
        this.loc2 = loc2;
    }
    
    
    public Location getLocation1() {
        return loc1;
    }
    
    public Location getLocation2() {
        return loc2;
    }
    
    
}
