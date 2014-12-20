package org.mle.nexgenkoths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mle.nexgenkoths.customitems.CustomItem;
import org.mle.nexgenkoths.events.PlayerCaptureKothEvent;
import org.mle.nexgenkoths.integration.Factions;
import org.mle.nexgenkoths.integration.Vault;
import org.mle.nexgenkoths.itemcollections.ItemCollection;
import org.mle.nexgenkoths.loottables.LootTable;
import org.mle.nexgenkoths.mleutils.InventoryUtils;
import org.mle.nexgenkoths.util.ScoreboardUtil;

public class NexGenKoths {
    
    public static List<Koth> loadedKoths = new ArrayList<Koth>();
    public static List<LootTable> loadedLootTables = new ArrayList<LootTable>();
    public static List<CustomItem> loadedCustomItems = new ArrayList<CustomItem>();
    public static List<ItemCollection> loadedItemCollections = new ArrayList<ItemCollection>();
    
    
    public static Map<UUID, Long> zoneEnterCooldownPlayers = new HashMap<UUID, Long>();
    
    public static Map<UUID, LocationPair> playerSelections = new HashMap<UUID, LocationPair>();
    public static Material selectionItem = Material.STICK;
    public static boolean selectOnlyInCreative = false;
    
    public static String kothCapStartMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{PLAYER}" + ChatColor.GOLD + " is trying to control " + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + "!";
    public static String kothCapStopMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{PLAYER}" + ChatColor.GOLD + " has lost control of " + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + "!";
    public static String zoneEnterCooldownMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED + "You can't enter another KoTH for {SECONDS} seconds.";
    public static String kothStartMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GREEN + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GREEN + " is now active!";
    public static String kothStopMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.RED + " is no longer active.";
    public static String kothCapturedMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{PLAYER}" + ChatColor.GOLD + " has captured {KOTH_NAME}!";
    
    public static long zoneEnterCooldown = 15;
    
    public static boolean canCaptureWhileInvis = false;
    
    public static boolean useScoreboard = true;
    public static String scoreboardObjDisplayName = ChatColor.LIGHT_PURPLE + "NexGen KoTHs";
    public static String belowNameObjDisplayName  = ChatColor.GOLD + "Controller";
    public static Map<UUID, Map<String, Integer>> playerScoreboardsMap = new HashMap<UUID, Map<String, Integer>>();
    public static Map<String, Integer> globalScoreboardsMap = new HashMap<String, Integer>();
    public static long scoreboardUpdateFrequency = 10;
    
    public static boolean autoUpdate = true;
    public static boolean sendMetrics = true;
    
    
    protected static void startTimers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable() {
            public void run() {
                Map<UUID, Long> zoneEnterCooldownsCopy = new HashMap<UUID, Long>(zoneEnterCooldownPlayers);
                
                for(Entry<UUID, Long> entry : zoneEnterCooldownsCopy.entrySet()) {
                    zoneEnterCooldownPlayers.put(entry.getKey(), entry.getValue() - 1);
                    
                    if(useScoreboard && Bukkit.getOfflinePlayer(entry.getKey()).isOnline()) {
                        if(playerScoreboardsMap.containsKey(entry.getKey())) {
                            playerScoreboardsMap.get(entry.getKey()).put(ChatColor.GREEN + "Enter Cooldown", entry.getValue().intValue());
                        } else {
                            Map<String, Integer> map = new HashMap<String, Integer>();
                            map.put(ChatColor.GREEN + "Enter Cooldown", entry.getValue().intValue());
                            
                            playerScoreboardsMap.put(entry.getKey(), map);
                        }
                    }
                    
                    if(entry.getValue().longValue() <= 0)
                        zoneEnterCooldownPlayers.remove(entry.getKey());
                }
            }
        }, 20, 20);
        
        
        if(useScoreboard)
            Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable() {
                public void run() {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        ScoreboardUtil.updateScoreboard(player, null);
                    }
                    
                    for(Entry<UUID, Map<String, Integer>> entry : playerScoreboardsMap.entrySet()) {
                        Player player = Bukkit.getPlayer(entry.getKey());
                        ScoreboardUtil.updateScoreboard(player, entry.getValue());
                    }
                }
            }, scoreboardUpdateFrequency, scoreboardUpdateFrequency);
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
    
    
    
    public static void onPlayerEnterKoth(Player player, Koth koth, PlayerMoveEvent e) {
        if(!koth.isActive()) return;
        
        if(!koth.isBeingCaptured()) {
            if(!canCaptureWhileInvis) {
                for(PotionEffect pe : player.getActivePotionEffects()) {
                    if(pe.getType().equals(PotionEffectType.INVISIBILITY))
                        return;
                }
            }
            
            koth.startCaptureTimer(player);
            Bukkit.broadcastMessage(kothCapStartMsg.replace("{KOTH_NAME}", koth.getName()).replace("{PLAYER}", player.getName()));
        }
    }
    
    
    public static void onPlayerExitKoth(Player player, Koth koth, PlayerMoveEvent e) {
        if(!koth.isActive()) return;
        
        if(koth.getCappingPlayer().equals(player)) {
            koth.stopCaptureTimer(player);
            Bukkit.broadcastMessage(kothCapStopMsg.replace("{KOTH_NAME}", koth.getName()).replace("{PLAYER}", player.getName()));
        }
        
        if(!player.hasPermission("nexgenkoths.entercooldown.bypass") && zoneEnterCooldown > 0)
            zoneEnterCooldownPlayers.put(player.getUniqueId(), zoneEnterCooldown);
    }
    
    
    public static boolean onPlayerCaptureKoth(Player player, Koth koth) {
        List<ItemStack> loot = new ArrayList<ItemStack>();
        Map<String, Double> nonItemLoot = new HashMap<String, Double>();
        
        if(koth.getFlagValue(KothFlag.USE_LOOT_TABLE) != 0 && koth.getLootTable() != null) {
            loot = koth.getRandomLoot();
            nonItemLoot = koth.getRandomNonItemLoot();
            
            if(loot == null) {
                P.log(Level.WARNING, "Error giving player \"" + player.getName() + "\" KoTH loot: KoTH \"" + koth.getName() + "\"'s random loot list returned null.");
                return false;
            }
            
            if(nonItemLoot == null) {
                P.log(Level.WARNING, "Error giving player \"" + player.getName() + "\" KoTH loot: KoTH \"" + koth.getName() + "\"'s random NON-ITEM loot list returned null.");
                return false;
            }
        }
        
        PlayerCaptureKothEvent event = new PlayerCaptureKothEvent(player, koth, loot, nonItemLoot);
        Bukkit.getPluginManager().callEvent(event);
        
        
        if(event.isCancelled())
            return false;
        
        for(ItemStack is : loot)
            InventoryUtils.givePlayerItemStack(player, is);
        
        for(Entry<String, Double> entry : nonItemLoot.entrySet()) {
            switch(entry.getKey().toLowerCase()) {
            
            case "money":
                Vault.givePlayerMoney(player, entry.getValue());
                break;
            case "factionspower":
                Factions.addPower(player, entry.getValue());
                break;
            case "exp":
                player.giveExp(entry.getValue().intValue());
                break;
            default:
                Bukkit.getLogger().warning("Unknown non-item loot: " + entry.getKey());
                break;
            
            }
        }
        
        if(koth.getFlagValue(KothFlag.BROADCAST_CAPTURE) != 0)
            Bukkit.broadcastMessage(kothCapturedMsg.replace("{KOTH_NAME}", koth.getName()).replace("{PLAYER}", player.getName()));
        
        return true;
    }
    
    
}
