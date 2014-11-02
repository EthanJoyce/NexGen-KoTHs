package org.mle.nexgenkoths.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.LocationPair;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.util.LocationUtils;

public class NexGenListener implements Listener {
    
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
            
            if(LocationUtils.isLocationInside(e.getTo(), koth.getCapZoneLocations())) { // Player entered KoTH capture zone
                if(NexGenKoths.zoneEnterCooldownPlayers.containsKey(e.getPlayer().getUniqueId())) {
                    e.setTo(e.getFrom());
                    
                    e.getPlayer().sendMessage(NexGenKoths.zoneEnterCooldownMsg.replace("{PLAYER}", e.getPlayer().getName()).replace("{KOTH_NAME}", koth.getName()).replace("{SECONDS}", NexGenKoths.zoneEnterCooldownPlayers.get(e.getPlayer().getUniqueId()).toString()));
                    
                    return;
                }
                
                NexGenKoths.onPlayerEnterKoth(e.getPlayer(), koth, e);
            }
            else if(koth.isBeingCaptured() && koth.getCappingPlayer().equals(e.getPlayer())) { // Player left KoTH capture zone
                NexGenKoths.onPlayerExitKoth(e.getPlayer(), koth, e);
            }
        }
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Koth kothPlayerCapping = NexGenKoths.getKothPlayerCapping(e.getPlayer());
        if(kothPlayerCapping != null)
            NexGenKoths.onPlayerExitKoth(e.getPlayer(), kothPlayerCapping, null);
        
        if(NexGenKoths.playerScoreboardsMap.containsKey(e.getPlayer().getUniqueId()))
            NexGenKoths.playerScoreboardsMap.remove(e.getPlayer().getUniqueId());
        
        if(NexGenKoths.playerSelections.containsKey(e.getPlayer().getUniqueId()))
            NexGenKoths.playerSelections.remove(e.getPlayer().getUniqueId());
    }
    
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        for(Koth koth : NexGenKoths.loadedKoths) {
            if(!koth.isActive()) continue;
            
            if(LocationUtils.isLocationInside(e.getTo(), koth.getCapZoneLocations())) { // Player teleported into the KoTH capture zone
                if(NexGenKoths.zoneEnterCooldownPlayers.containsKey(e.getPlayer().getUniqueId())) {
                    e.setTo(e.getFrom());
                    
                    e.getPlayer().sendMessage(NexGenKoths.zoneEnterCooldownMsg.replace("{PLAYER}", e.getPlayer().getName()).replace("{KOTH_NAME}", koth.getName()).replace("{SECONDS}", NexGenKoths.zoneEnterCooldownPlayers.get(e.getPlayer().getUniqueId()).toString()));
                    
                    return;
                }
                
                NexGenKoths.onPlayerEnterKoth(e.getPlayer(), koth, e);
            }
            else if(koth.isBeingCaptured() && koth.getCappingPlayer().equals(e.getPlayer())) { // Player teleported out of the KoTH capture zone
                NexGenKoths.onPlayerExitKoth(e.getPlayer(), koth, e);
            }
        }
    }
    
    
}
