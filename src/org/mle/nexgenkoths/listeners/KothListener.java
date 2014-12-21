package org.mle.nexgenkoths.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.KothFlag;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.P;
import org.mle.nexgenkoths.events.PlayerCaptureKothEvent;
import org.mle.nexgenkoths.events.PlayerEnterKothEvent;
import org.mle.nexgenkoths.events.PlayerExitKothEvent;

public class KothListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerEnterKoth(PlayerEnterKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();
        
        if(NexGenKoths.zoneCaptureCooldownPlayers.containsKey(e.getPlayer().getUniqueId()) && NexGenKoths.zoneCaptureCooldownPlayers.get(e.getPlayer().getUniqueId()).longValue() > 0) {
        	e.getPlayer().sendMessage(NexGenKoths.zoneCaptureCooldownMsg.replace("{PLAYER}", e.getPlayer().getName()).replace("{KOTH_NAME}", koth.getName()).replace("{SECONDS}", NexGenKoths.zoneCaptureCooldownPlayers.get(e.getPlayer().getUniqueId()).toString()));
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
            Bukkit.broadcastMessage(NexGenKoths.kothCapStartMsg.replace("{KOTH_NAME}", koth.getName()).replace("{PLAYER}", player.getName()));
        }
    }
	
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerExitKoth(PlayerExitKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();
        
        if(koth.getCappingPlayer() != null && koth.getCappingPlayer().equals(player)) {
            koth.stopCaptureTimer(player);
            Bukkit.broadcastMessage(NexGenKoths.kothCapStopMsg.replace("{KOTH_NAME}", koth.getName()).replace("{PLAYER}", player.getName()));
        	
        	if(!player.hasPermission("nexgenkoths.entercooldown.bypass") && NexGenKoths.zoneCaptureCooldown > 0)
            	NexGenKoths.zoneCaptureCooldownPlayers.put(player.getUniqueId(), NexGenKoths.zoneCaptureCooldown);
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
        
        e.setCancelled(true);
        
        e.setLoot(loot);
        e.setNonItemLoot(nonItemLoot);
    }
    
	
}
