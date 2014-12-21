package org.mle.nexgenkoths.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.LocationPair;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.events.PlayerEnterKothEvent;
import org.mle.nexgenkoths.events.PlayerExitKothEvent;
import org.mle.nexgenkoths.util.LocationUtils;

public class PlayerListener implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        
        if(player.hasPermission("nexgenkoths.create") && e.getItem() != null && e.getItem().getType().equals(NexGenKoths.selectionItem) && e.getClickedBlock() != null) {
            if(NexGenKoths.selectOnlyInCreative && !player.getGameMode().equals(GameMode.CREATIVE)) return;
            
            final Location clickedLocation = e.getClickedBlock().getLocation();
            String message = null;
            
            if(!NexGenKoths.playerSelections.containsKey(player.getUniqueId()))
                NexGenKoths.playerSelections.put(player.getUniqueId(), new LocationPair(null, null));
            
            if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                message  = String.format(ChatColor.GREEN + "Location 1 set to X: %s, Y: %s, Z: %s, World: \"%s\"", clickedLocation.getBlockX(), clickedLocation.getBlockY(), clickedLocation.getBlockZ(), clickedLocation.getWorld().getName());
                
                if(NexGenKoths.playerSelections.get(player.getUniqueId()).getLocation1() == null || !NexGenKoths.playerSelections.get(player.getUniqueId()).getLocation1().equals(clickedLocation))
                    NexGenKoths.playerSelections.get(player.getUniqueId()).setLocation1(clickedLocation);
                else
                    message = ChatColor.RED + "Location 1 is already set here.";
            }
            else if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            	message  = String.format(ChatColor.GREEN + "Location 2 set to X: %s, Y: %s, Z: %s, World: \"%s\"", clickedLocation.getBlockX(), clickedLocation.getBlockY(), clickedLocation.getBlockZ(), clickedLocation.getWorld().getName());
                
                if(NexGenKoths.playerSelections.get(player.getUniqueId()).getLocation2() == null || !NexGenKoths.playerSelections.get(player.getUniqueId()).getLocation2().equals(clickedLocation))
                    NexGenKoths.playerSelections.get(player.getUniqueId()).setLocation2(clickedLocation);
                else
                    message = ChatColor.RED + "Location 2 is already set here.";
            }
            
            if(message != null) {
                player.sendMessage(message);
                e.setCancelled(true);
            }
        }
    }
    
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        for(Koth koth : NexGenKoths.loadedKoths) {
            if(!koth.isActive()) continue;
            
            if(LocationUtils.isLocationInside(e.getTo(), koth.getCapZoneLocations()) && !LocationUtils.isLocationInside(e.getFrom(), koth.getCapZoneLocations())) { // Player entered KoTH capture zone
                PlayerEnterKothEvent event = new PlayerEnterKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());
                
                Bukkit.getPluginManager().callEvent(event);
                
                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            }
            else if(!LocationUtils.isLocationInside(e.getTo(), koth.getCapZoneLocations()) && LocationUtils.isLocationInside(e.getFrom(), koth.getCapZoneLocations())) { // Player left KoTH capture zone
                PlayerExitKothEvent event = new PlayerExitKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());
                
                Bukkit.getPluginManager().callEvent(event);
                
                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            }
        }
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Koth kothPlayerCapping = NexGenKoths.getKothPlayerCapping(e.getPlayer());
        if(kothPlayerCapping != null)
            kothPlayerCapping.stopCaptureTimer(e.getPlayer());
        
        if(NexGenKoths.playerScoreboardsMap.containsKey(e.getPlayer().getUniqueId()))
            NexGenKoths.playerScoreboardsMap.remove(e.getPlayer().getUniqueId());
        
        if(NexGenKoths.playerSelections.containsKey(e.getPlayer().getUniqueId()))
            NexGenKoths.playerSelections.remove(e.getPlayer().getUniqueId());
    }
    
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        for(Koth koth : NexGenKoths.loadedKoths) {
            if(!koth.isActive()) continue;
            
            if(LocationUtils.isLocationInside(e.getTo(), koth.getCapZoneLocations()) && !LocationUtils.isLocationInside(e.getFrom(), koth.getCapZoneLocations())) { // Player entered KoTH capture zone
                PlayerEnterKothEvent event = new PlayerEnterKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());
                
                Bukkit.getPluginManager().callEvent(event);
                
                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            }
            else if(!LocationUtils.isLocationInside(e.getTo(), koth.getCapZoneLocations()) && LocationUtils.isLocationInside(e.getFrom(), koth.getCapZoneLocations())) { // Player left KoTH capture zone
                PlayerExitKothEvent event = new PlayerExitKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());
                
                Bukkit.getPluginManager().callEvent(event);
                
                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            }
        }
    }
    
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
    	Koth koth = NexGenKoths.getKothPlayerCapping(e.getEntity());
    	
    	if(koth != null)
    		koth.stopCaptureTimer(e.getEntity());
    }
    
    
}
