package com.mrlolethan.nexgenkoths.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.mrlolethan.nexgenkoths.koth.Koth;

public class PlayerStartCaptureKothEvent extends PlayerKothEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	
	public PlayerStartCaptureKothEvent(Player player, Koth koth) {
		super(player, koth);
	}
	
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	
    @Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
    
}
