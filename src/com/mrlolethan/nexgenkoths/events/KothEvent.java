package com.mrlolethan.nexgenkoths.events;

import org.bukkit.event.Event;

import com.mrlolethan.nexgenkoths.Koth;

public abstract class KothEvent extends Event {
	
	protected Koth koth;
	
	public KothEvent(Koth koth) {
		this.koth = koth;
	}
	
	
	public Koth getKoth() {
		return koth;
	}
	
}
