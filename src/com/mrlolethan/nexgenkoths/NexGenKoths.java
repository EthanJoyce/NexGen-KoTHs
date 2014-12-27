package com.mrlolethan.nexgenkoths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mrlolethan.nexgenkoths.customitems.CustomItem;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollection;
import com.mrlolethan.nexgenkoths.loottables.LootTable;
import com.mrlolethan.nexgenkoths.scoreboard.ScoreboardHandler;

public class NexGenKoths {
    
    public static List<Koth> loadedKoths = new ArrayList<Koth>();
    public static List<LootTable> loadedLootTables = new ArrayList<LootTable>();
    public static List<CustomItem> loadedCustomItems = new ArrayList<CustomItem>();
    public static List<ItemCollection> loadedItemCollections = new ArrayList<ItemCollection>();
    
    static ScoreboardHandler scoreboardHandler;
    
    
    private static Map<UUID, Long> zoneCaptureCooldownPlayers = new HashMap<UUID, Long>();
    
    public static Map<UUID, LocationPair> playerSelections = new HashMap<UUID, LocationPair>();
    public static Material selectionItem = Material.STICK;
    public static boolean selectOnlyInCreative = false;
    
    public static String kothCapStartMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{PLAYER}" + ChatColor.GOLD + " is trying to control " + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + "!";
    public static String kothCapStopMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{PLAYER}" + ChatColor.GOLD + " has lost control of " + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + "!";
    public static String zoneCaptureCooldownMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED + "You can't attempt to control another KoTH for {SECONDS} seconds.";
    public static String kothStartMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GREEN + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GREEN + " is now active!";
    public static String kothStopMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.RED + " is no longer active.";
    public static String kothCapturedMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{PLAYER}" + ChatColor.GOLD + " has controlled {KOTH_NAME}!";
    
    public static long zoneCaptureCooldown = 15;
    public static boolean preventEntryOnCaptureCooldown = false;
    
    public static boolean canCaptureWhileInvis = false;
    
    static boolean useScoreboard = true;
    public static String scoreboardObjDisplayName = ChatColor.LIGHT_PURPLE + "NexGen KoTHs";
    public static String belowNameObjDisplayName  = ChatColor.GOLD + "Controller";
    public static long scoreboardUpdateFrequency = 20;
    
    public static boolean autoUpdate = false;
    public static boolean sendMetrics = true;
    
    
    private static String staffWarning = null;
    
    
    protected static void startTimers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable() {
            public void run() {
                Map<UUID, Long> zoneCaptureCooldownsCopy = new HashMap<UUID, Long>(zoneCaptureCooldownPlayers);
                
                for(Entry<UUID, Long> entry : zoneCaptureCooldownsCopy.entrySet()) {
                    zoneCaptureCooldownPlayers.put(entry.getKey(), entry.getValue() - 1);
                    
                    if(entry.getValue().longValue() <= 0)
                        zoneCaptureCooldownPlayers.remove(entry.getKey());
                }
            }
        }, 20, 20);
    }
    
    
    public static Koth getKothByName(String name) {
        for(Koth koth : loadedKoths) {
            if(koth.getName().equalsIgnoreCase(name))
                return koth;
        }
        
        return null;
    }
    
    
    public static LootTable getLootTableByName(String name) {
        for(LootTable table : loadedLootTables) {
            if(table.getName().equalsIgnoreCase(name))
                return table;
        }
        
        return null;
    }
    
    
    public static CustomItem getCustomItemByName(String name) {
        for(CustomItem customItem : loadedCustomItems) {
            if(customItem.getName().equalsIgnoreCase(name))
                return customItem;
        }
        
        return null;
    }
    
    
    public static ItemCollection getItemCollectionByName(String name) {
        for(ItemCollection coll : loadedItemCollections) {
            if(coll.getName().equalsIgnoreCase(name))
                return coll;
        }
        
        return null;
    }
    
    
    public static Koth getKothPlayerCapping(Player player) {
        for(Koth koth : loadedKoths) {
            if(koth.isBeingCaptured() && koth.getCappingPlayer().equals(player))
                return koth;
        }
        
        return null;
    }
    
    
    public static void setStaffWarning(String msg) {
    	staffWarning = msg;
    	
    	new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    				if(NexGenKoths.staffWarning != null && (player.isOp() || player.hasPermission("nexgenkoths.cmd.main")))
    					player.sendMessage(NexGenKoths.staffWarning);
    			}
			}
		}.runTaskLater(P.p, 20);
    }
    
    public static String getStaffWarning() {
    	return staffWarning;
    }
    
    
    public static boolean isOnCaptureCooldown(Player player) {
    	return zoneCaptureCooldownPlayers.containsKey(player.getUniqueId());
	}
	
	public static long getCaptureCooldownRemaining(Player player) {
		return zoneCaptureCooldownPlayers.get(player.getUniqueId());
	}
	
	public static void putOnCaptureCooldown(Player player) {
		zoneCaptureCooldownPlayers.put(player.getUniqueId(), zoneCaptureCooldown);
	}
	
	
	public static boolean isUsingScoreboard() {
		return scoreboardHandler != null;
	}
	
	public static ScoreboardHandler getScoreboardHandler() {
		return scoreboardHandler;
	}
    
    
}
