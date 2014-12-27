package com.mrlolethan.nexgenkoths.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.mrlolethan.nexgenkoths.Koth;

public class PlayerEnterKothEvent extends PlayerKothEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	private Location from;
	private Location to;
	
	
	public PlayerEnterKothEvent(Player player, Koth koth, Location from, Location to) {
		super(player, koth);
		
		this.from = from;
		this.to = to;
	}
	
	
	public Location getFrom() {
		return from;
	}
	
	public void setFrom(Location from) {
		this.from = from;
	}
	
	
	public Location getTo() {
		return to;
	}
	
	public void setTo(Location to) {
		this.to = to;
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
