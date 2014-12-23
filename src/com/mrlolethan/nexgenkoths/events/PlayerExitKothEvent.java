package com.mrlolethan.nexgenkoths.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mrlolethan.nexgenkoths.Koth;

public class PlayerExitKothEvent extends PlayerMoveEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	
	private Koth koth;
	
	public PlayerExitKothEvent(Player player, Koth koth, Location from, Location to) {
		super(player, from, to);
		
	    this.koth = koth;
	}
	
	
	public Koth getKoth() {
	    return koth;
	}
	
	
    @Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
    
}
