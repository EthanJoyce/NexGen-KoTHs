package org.mle.nexgenkoths;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.gravitydevelopment.updater.nexgenkoths.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcstats.nexgenkoths.Metrics;
import org.mle.nexgenkoths.commands.KothCommandExecutor;
import org.mle.nexgenkoths.customitems.CustomItem;
import org.mle.nexgenkoths.customitems.CustomItemsDataHandler;
import org.mle.nexgenkoths.listeners.NexGenListener;
import org.mle.nexgenkoths.loottables.LootTable;
import org.mle.nexgenkoths.loottables.LootTableDataHandler;
import org.mle.nexgenkoths.util.ScoreboardUtil;

public class NexGenKoths extends JavaPlugin {
    
    public static NexGenKoths instance;
    public static String tag;
    public static File pluginFile;
    
    public static List<Koth> loadedKoths = new ArrayList<Koth>();
    public static List<LootTable> loadedLootTables = new ArrayList<LootTable>();
    public static List<CustomItem> loadedCustomItems = new ArrayList<CustomItem>();
    
    
    public static Map<UUID, Long> zoneEnterCooldownPlayers = new HashMap<UUID, Long>();
    
    public static Map<UUID, LocationPair> playerSelections = new HashMap<UUID, LocationPair>();
    public static Material selectionItem = Material.STICK;
    public static boolean selectOnlyInCreative = false;
    
    public static String kothCapStartMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED.toString() + ChatColor.BOLD + "{PLAYER} has started to capture {KOTH_NAME}!";
    public static String kothCapStopMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GREEN.toString() + ChatColor.BOLD + "{PLAYER} has left the {KOTH_NAME} KoTH!";
    public static String zoneEnterCooldownMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED + "You can't enter another KoTH for {SECONDS} seconds.";
    public static String kothStartMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GREEN.toString() + ChatColor.BOLD + "{KOTH_NAME} is now active!";
    public static String kothStopMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.RED.toString() + ChatColor.BOLD + "{KOTH_NAME} is no longer active.";
    public static String kothCapturedMsg = ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GREEN.toString() + ChatColor.BOLD + "{PLAYER} has captured {KOTH_NAME}!";
    
    public static long zoneEnterCooldown = 15;
    
    public static boolean canCaptureWhileInvis = false;
    
    public static boolean useScoreboard = true;
    public static String scoreboardObjDisplayName = ChatColor.LIGHT_PURPLE + "NexGen KoTHs";
    public static Map<UUID, Map<String, Integer>> playerScoreboardsMap = new HashMap<UUID, Map<String, Integer>>();
    public static Map<String, Integer> globalScoreboardsMap = new HashMap<String, Integer>();
    public static long scoreboardUpdateFrequency = 10;
    
    public static boolean autoUpdate = true;
    public static boolean sendMetrics = true;
    
    
    public void onEnable() {
        instance = this;
        tag = "[" + getDescription().getName() + "]";
        pluginFile = getFile();
        
        CustomItemsDataHandler.initDirectories();
        LootTableDataHandler.initDirectories();
        KothDataHandler.initDirectories();
        
        getCommand("koth").setExecutor(new KothCommandExecutor());
        
        Bukkit.getPluginManager().registerEvents(new NexGenListener(), this);
        
        if(!(new File(getDataFolder(), "config.yml")).exists()) {
            CustomItemsDataHandler.createExampleCustomItems();
            LootTableDataHandler.createExampleTable();
        }
        
        initConfiguration();
        
        try {
            loadConfiguration();
        } catch(InvalidConfigurationException ex) {
            Bukkit.getLogger().severe(tag + " Error loading config: " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        CustomItemsDataHandler.loadAllCustomItems();
        LootTableDataHandler.loadAllLootTables();
        KothDataHandler.loadAllKoths();
        startTimers();
        
        if(sendMetrics) {
            try {
		        Metrics metrics = new Metrics(this);
		        metrics.start();
		    } catch(IOException ex) {
		        ex.printStackTrace();
		        getServer().getLogger().severe(tag + " Error starting Metrics: \"" + ex.getMessage() + "\"");
	    	}
        }
        if(autoUpdate)
            new Updater(this, 86133, getFile(), Updater.UpdateType.DEFAULT, false);
    }
    
    
    public void onDisable() {
        KothDataHandler.saveAllKoths();
    }
    
    
    private void startTimers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
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
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
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
    
    
    public void initConfiguration() {
        getConfig().addDefault("KoTHs.Area_Selection.Item", "STICK");
        getConfig().addDefault("KoTHs.Area_Selection.OnlyInCreative", selectOnlyInCreative);
        
        getConfig().addDefault("KoTHs.KoTH_Capture.ZoneEnterCooldown", zoneEnterCooldown);
        getConfig().addDefault("KoTHs.KoTH_Capture.ZoneEnterCooldown_Message", zoneEnterCooldownMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.KoTH_Capture_Start_Message", kothCapStartMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.KoTH_Capture_Stop_Message", kothCapStopMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.KoTH_Captured_Message", kothCapturedMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.CanPlayerCaptureWhileInvisible", canCaptureWhileInvis);
        
        getConfig().addDefault("KoTHs.KoTH_Start_Message", kothStartMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Stop_Message", kothStopMsg.replace(ChatColor.COLOR_CHAR, '&'));
        
        getConfig().addDefault("KoTHs.Scoreboard.Use", useScoreboard);
        getConfig().addDefault("KoTHs.Scoreboard.DisplayName", scoreboardObjDisplayName.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.Scoreboard.UpdateFrequency", scoreboardUpdateFrequency);
        
        getConfig().addDefault("AutoUpdate", autoUpdate);
        getConfig().addDefault("SendMetrics", sendMetrics);
        
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    
    
    public void loadConfiguration() throws InvalidConfigurationException {
        String selectionItemStr = getConfig().getString("KoTHs.Area_Selection.Item");
        
        try {
            selectionItem = Material.valueOf(selectionItemStr);
        } catch(IllegalArgumentException ex) {
            throw new InvalidConfigurationException("Selector item (KoTHs.Area_Selection.Item) is not a valid Material: \"" + selectionItemStr + "\"");
        }
        
        selectOnlyInCreative = getConfig().getBoolean("KoTHs.Area_Selection.OnlyInCreative", selectOnlyInCreative);
        
        
        zoneEnterCooldown = getConfig().getLong("KoTHs.KoTH_Capture.ZoneEnterCooldown", zoneEnterCooldown);
        zoneEnterCooldownMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.ZoneEnterCooldown_Message", zoneEnterCooldownMsg));
        kothCapStartMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Capture_Start_Message", kothCapStartMsg));
        kothCapStopMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Capture_Stop_Message", kothCapStopMsg));
        kothCapturedMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Captured_Message", kothCapturedMsg));
        canCaptureWhileInvis = getConfig().getBoolean("KoTHs.KoTH_Capture.CanPlayerCaptureWhileInvisible", canCaptureWhileInvis);
        
        kothStartMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Start_Message", kothStartMsg));
        kothStopMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Stop_Message", kothStopMsg));
        
        useScoreboard = getConfig().getBoolean("KoTHs.Scoreboard.Use", useScoreboard);
        scoreboardObjDisplayName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.Scoreboard.DisplayName", scoreboardObjDisplayName));
        scoreboardUpdateFrequency = getConfig().getLong("KoTHs.Scoreboard.UpdateFrequency", scoreboardUpdateFrequency);
        
        autoUpdate = getConfig().getBoolean("AutoUpdate", autoUpdate);
        sendMetrics = getConfig().getBoolean("SendMetrics", sendMetrics);
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
        
        if(!player.hasPermission("nexgenkoths.entercooldown.bypass"))
            zoneEnterCooldownPlayers.put(player.getUniqueId(), zoneEnterCooldown);
    }
    
    
    public static void onPlayerCaptureKoth(Player player, Koth koth) {
        if(koth.getFlags().containsKey(KothFlag.USE_LOOT_TABLE) && koth.getFlags().get(KothFlag.USE_LOOT_TABLE) != 0 && koth.getLootTable() != null) {
            List<ItemStack> randomLoot = koth.getRandomLoot();
            
            if(randomLoot == null) {
                Bukkit.getLogger().warning(tag + " Error giving player \"" + player.getName() + "\" KoTH loot: KoTH \"" + koth.getName() + "\"'s random loot list returned null.");
                return;
            }
            
            for(ItemStack is : randomLoot)
                player.getInventory().addItem(is);
        }
        
        Bukkit.broadcastMessage(kothCapturedMsg.replace("{KOTH_NAME}", koth.getName()).replace("{PLAYER}", player.getName()));
    }
    
    
}
