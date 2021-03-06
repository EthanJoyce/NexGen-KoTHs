package com.mrlolethan.nexgenkoths.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.P;
import com.mrlolethan.nexgenkoths.Permissions;
import com.mrlolethan.nexgenkoths.events.PlayerCaptureKothEvent;
import com.mrlolethan.nexgenkoths.events.PlayerEnterKothEvent;
import com.mrlolethan.nexgenkoths.events.PlayerExitKothEvent;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothFlag;

public class KothListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerEnterKoth(PlayerEnterKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();
        
        if(NexGenKoths.isOnCaptureCooldown(player) && NexGenKoths.getCaptureCooldownRemaining(player) > 0) {
        	if(NexGenKoths.preventEntryOnCaptureCooldown)
        		e.setCancelled(true);
        	
        	player.sendMessage(
        		NexGenKoths.zoneCaptureCooldownMsg
        			.replace("{PLAYER}", e.getPlayer().getName())
        			.replace("{KOTH_NAME}", koth.getName())
        			.replace("{SECONDS}", Long.toString(NexGenKoths.getCaptureCooldownRemaining(player)))
        	);
        	return;
        }
        
        if(!koth.isBeingCaptured()) {
            if(!NexGenKoths.canCaptureWhileInvis) {
                for(PotionEffect pe : player.getActivePotionEffects()) {
                    if(pe.getType().equals(PotionEffectType.INVISIBILITY))
                        return;
                }
            }
            
            koth.startCaptureTimer(player);
        }
    }
	
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerExitKoth(PlayerExitKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();
        
        if(koth.getCappingPlayer() != null && koth.getCappingPlayer().equals(player)) {
            koth.stopCaptureTimer(player);
        	
        	if(!Permissions.bypassCaptureCooldown(player) && NexGenKoths.zoneCaptureCooldown > 0)
            	NexGenKoths.putOnCaptureCooldown(player);
        }
    }
    
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerCaptureKoth(PlayerCaptureKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();
        
        List<ItemStack> loot = new ArrayList<ItemStack>();
        Map<String, Double> nonItemLoot = new HashMap<String, Double>();
        
        if(koth.getFlagValue(KothFlag.USE_LOOT_TABLE) != 0 && koth.getLootTable() != null) {
            loot = koth.getRandomLoot();
            nonItemLoot = koth.getRandomNonItemLoot();
            
            if(loot == null) {
                P.log(Level.WARNING, "Error giving player \"" + player.getName() + "\" KoTH loot: KoTH \"" + koth.getName() + "\"'s random loot list returned null.");
                return;
            }
            
            if(nonItemLoot == null) {
                P.log(Level.WARNING, "Error giving player \"" + player.getName() + "\" KoTH loot: KoTH \"" + koth.getName() + "\"'s random NON-ITEM loot list returned null.");
                return;
            }
        }
        
        e.setLoot(loot);
        e.setNonItemLoot(nonItemLoot);
    }
    
	
}
