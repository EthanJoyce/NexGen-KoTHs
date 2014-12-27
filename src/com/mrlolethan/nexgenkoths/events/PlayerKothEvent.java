package com.mrlolethan.nexgenkoths.events;

import org.bukkit.entity.Player;

import com.mrlolethan.nexgenkoths.Koth;

public abstract class PlayerKothEvent extends KothEvent {
	
	private Player player;
	
	public PlayerKothEvent(Player player, Koth koth) {
		super(koth);
		
		this.player = player;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
}
