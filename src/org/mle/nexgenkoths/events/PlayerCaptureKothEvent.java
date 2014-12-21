package org.mle.nexgenkoths.events;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.Koth;

public class PlayerCaptureKothEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	private Koth koth;
	private List<ItemStack> loot;
	private Map<String, Double> nonItemLoot;
	
	
	public PlayerCaptureKothEvent(Player player, Koth koth, List<ItemStack> loot, Map<String, Double> nonItemLoot) {
		super(player);
		
	    this.koth = koth;
	    this.loot = loot;
	    this.nonItemLoot = nonItemLoot;
	}
	
	
	public Koth getKoth() {
	    return koth;
	}
	
	public List<ItemStack> getLoot() {
	    return Collections.unmodifiableList(loot);
	}
	
	public Map<String, Double> getNonItemLoot() {
	    return Collections.unmodifiableMap(nonItemLoot);
	}
	
	
	public void setLoot(List<ItemStack> loot) {
	    this.loot = loot;
	}
	
	public void addLoot(ItemStack itemStack) {
	    loot.add(itemStack);
	}
	
	public void removeLoot(ItemStack itemStack) {
	    loot.remove(itemStack);
	}
	
	
	public void setNonItemLoot(Map<String, Double> nonItemLoot) {
	    this.nonItemLoot = nonItemLoot;
	}
	
	public void addLoot(String name, double amount) {
	    nonItemLoot.put(name, amount);
	}
	
	public void removeLoot(String name) {
	    nonItemLoot.remove(name);
	}
	
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
    
    @Override
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
