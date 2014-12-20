package org.mle.nexgenkoths;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.gravitydevelopment.updater.nexgenkoths.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.nexgenkoths.Metrics;
import org.mle.nexgenkoths.commands.KothCommandExecutor;
import org.mle.nexgenkoths.customitems.CustomItemsDataHandler;
import org.mle.nexgenkoths.integration.Vault;
import org.mle.nexgenkoths.itemcollections.ItemCollectionDataHandler;
import org.mle.nexgenkoths.listeners.NexGenListener;
import org.mle.nexgenkoths.loottables.LootTableDataHandler;
import org.mle.nexgenkoths.util.ScoreboardUtil;

public class P extends JavaPlugin {
    
    public static P p;
    
    public static String tag;
    public static File pluginFile;
    
    
    public void onEnable() {
        p = this;
        
        if(!Vault.setupEconomy())
            Bukkit.getLogger().info(tag + " Vault economy setup failed; Vault features will not be present.");
        
        CustomItemsDataHandler.initDirectories();
        ItemCollectionDataHandler.initDirectories();
        LootTableDataHandler.initDirectories();
        KothDataHandler.initDirectories();
        
        getCommand("koth").setExecutor(new KothCommandExecutor());
        
        Bukkit.getPluginManager().registerEvents(new NexGenListener(), this);
        
        if(!(new File(getDataFolder(), "config.yml")).exists()) {
            CustomItemsDataHandler.createExampleCustomItems();
            ItemCollectionDataHandler.createExampleItemCollection();
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
        ItemCollectionDataHandler.loadAllItemCollections();
        LootTableDataHandler.loadAllLootTables();
        KothDataHandler.loadAllKoths();
        NexGenKoths.startTimers();
        
        if(NexGenKoths.sendMetrics) {
            try {
		        Metrics metrics = new Metrics(this);
		        metrics.start();
		    } catch(IOException ex) {
		        ex.printStackTrace();
		        getServer().getLogger().severe(tag + " Error starting Metrics: \"" + ex.getMessage() + "\"");
	    	}
        }
        if(NexGenKoths.autoUpdate)
            new Updater(this, 86133, getFile(), Updater.UpdateType.DEFAULT, false);
    }
    
    
    public void onDisable() {
        KothDataHandler.saveAllKoths();
        
        for(Player player : Bukkit.getServer().getOnlinePlayers())
            ScoreboardUtil.clearScoreboard(player);
    }
    
    
    public void initConfiguration() {
        getConfig().addDefault("KoTHs.Area_Selection.Item", NexGenKoths.selectionItem.toString());
        getConfig().addDefault("KoTHs.Area_Selection.OnlyInCreative", NexGenKoths.selectOnlyInCreative);
        
        getConfig().addDefault("KoTHs.KoTH_Capture.ZoneEnterCooldown", NexGenKoths.zoneEnterCooldown);
        getConfig().addDefault("KoTHs.KoTH_Capture.ZoneEnterCooldown_Message", NexGenKoths.zoneEnterCooldownMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.KoTH_Capture_Start_Message", NexGenKoths.kothCapStartMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.KoTH_Capture_Stop_Message", NexGenKoths.kothCapStopMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.KoTH_Captured_Message", NexGenKoths.kothCapturedMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Capture.CanPlayerCaptureWhileInvisible", NexGenKoths.canCaptureWhileInvis);
        
        getConfig().addDefault("KoTHs.KoTH_Start_Message", NexGenKoths.kothStartMsg.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.KoTH_Stop_Message", NexGenKoths.kothStopMsg.replace(ChatColor.COLOR_CHAR, '&'));
        
        getConfig().addDefault("KoTHs.Scoreboard.Use", NexGenKoths.useScoreboard);
        getConfig().addDefault("KoTHs.Scoreboard.DisplayName", NexGenKoths.scoreboardObjDisplayName.replace(ChatColor.COLOR_CHAR, '&'));
        getConfig().addDefault("KoTHs.Scoreboard.UpdateFrequency", NexGenKoths.scoreboardUpdateFrequency);
        
        getConfig().addDefault("AutoUpdate", NexGenKoths.autoUpdate);
        getConfig().addDefault("SendMetrics", NexGenKoths.sendMetrics);
        
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    
    
    public void loadConfiguration() throws InvalidConfigurationException {
        String selectionItemStr = getConfig().getString("KoTHs.Area_Selection.Item");
        
        try {
            NexGenKoths.selectionItem = Material.valueOf(selectionItemStr);
        } catch(IllegalArgumentException ex) {
            throw new InvalidConfigurationException("Selector item (KoTHs.Area_Selection.Item) is not a valid Material: \"" + selectionItemStr + "\"");
        }
        
        NexGenKoths.selectOnlyInCreative = getConfig().getBoolean("KoTHs.Area_Selection.OnlyInCreative", NexGenKoths.selectOnlyInCreative);
        
        
        NexGenKoths.zoneEnterCooldown = getConfig().getLong("KoTHs.KoTH_Capture.ZoneEnterCooldown", NexGenKoths.zoneEnterCooldown);
        NexGenKoths.zoneEnterCooldownMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.ZoneEnterCooldown_Message", NexGenKoths.zoneEnterCooldownMsg));
        NexGenKoths.kothCapStartMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Capture_Start_Message", NexGenKoths.kothCapStartMsg));
        NexGenKoths.kothCapStopMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Capture_Stop_Message", NexGenKoths.kothCapStopMsg));
        NexGenKoths.kothCapturedMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Captured_Message", NexGenKoths.kothCapturedMsg));
        NexGenKoths.canCaptureWhileInvis = getConfig().getBoolean("KoTHs.KoTH_Capture.CanPlayerCaptureWhileInvisible", NexGenKoths.canCaptureWhileInvis);
        
        NexGenKoths.kothStartMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Start_Message", NexGenKoths.kothStartMsg));
        NexGenKoths.kothStopMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Stop_Message", NexGenKoths.kothStopMsg));
        
        NexGenKoths.useScoreboard = getConfig().getBoolean("KoTHs.Scoreboard.Use", NexGenKoths.useScoreboard);
        NexGenKoths.scoreboardObjDisplayName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.Scoreboard.DisplayName", NexGenKoths.scoreboardObjDisplayName));
        NexGenKoths.scoreboardUpdateFrequency = getConfig().getLong("KoTHs.Scoreboard.UpdateFrequency", NexGenKoths.scoreboardUpdateFrequency);
        
        NexGenKoths.autoUpdate = getConfig().getBoolean("AutoUpdate", NexGenKoths.autoUpdate);
        NexGenKoths.sendMetrics = getConfig().getBoolean("SendMetrics", NexGenKoths.sendMetrics);
    }
    
    
    public static void log(Level level, String message) {
        Bukkit.getServer().getLogger().log(level, tag + " " + message);
    }
    
    
    {
        tag = "[" + getDescription().getName() + "]";
        pluginFile = getFile();
    }
}
