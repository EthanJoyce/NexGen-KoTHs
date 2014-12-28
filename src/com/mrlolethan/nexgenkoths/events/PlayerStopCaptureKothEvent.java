package com.mrlolethan.nexgenkoths.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.mrlolethan.nexgenkoths.koth.Koth;

public class PlayerStopCaptureKothEvent extends PlayerKothEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	
	public PlayerStopCaptureKothEvent(Player player, Koth koth) {
		super(player, koth);
	}
	
	
    @Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
    
}
