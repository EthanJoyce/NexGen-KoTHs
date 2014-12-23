package com.mrlolethan.nexgenkoths;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.gravitydevelopment.updater.nexgenkoths.Updater;
import net.minecraft.util.org.apache.commons.io.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.nexgenkoths.Metrics;

import com.mrlolethan.nexgenkoths.commands.KothCommandExecutor;
import com.mrlolethan.nexgenkoths.customitems.CustomItemsDataHandler;
import com.mrlolethan.nexgenkoths.integration.Vault;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollectionDataHandler;
import com.mrlolethan.nexgenkoths.listeners.KothListener;
import com.mrlolethan.nexgenkoths.listeners.PlayerListener;
import com.mrlolethan.nexgenkoths.loottables.LootTableDataHandler;
import com.mrlolethan.nexgenkoths.util.ScoreboardUtil;

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
        
        if(!(new File(getDataFolder(), "config.yml")).exists()) {
        	CustomItemsDataHandler.createExampleCustomItems();
        	ItemCollectionDataHandler.createExampleItemCollection();
        	LootTableDataHandler.createExampleTable();
        }
        
        if(!initConfiguration())
        	return;
        
        getCommand("koth").setExecutor(new KothCommandExecutor());
        
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new KothListener(), this);
        
        
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
        
        performUpdateCheck(NexGenKoths.autoUpdate);
    }
    
    
    public void onDisable() {
        KothDataHandler.saveAllKoths();
        
        for(Player player : Bukkit.getServer().getOnlinePlayers())
            ScoreboardUtil.clearScoreboard(player);
    }
    
    
    public boolean initConfiguration() {
        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {
        	try {
        		configFile.createNewFile();
        		FileUtils.copyInputStreamToFile(P.class.getResourceAsStream("/config.yml"), configFile);
        	} catch(IOException ex) {
        		ex.printStackTrace();
        		P.log(Level.SEVERE, "Error creating configuration file: " + ex.getMessage());
        		return false;
        	}
        }
        return true;
    }
    
    
    public void loadConfiguration() throws InvalidConfigurationException {
        String selectionItemStr = getConfig().getString("KoTHs.Area_Selection.Item");
        
        try {
            NexGenKoths.selectionItem = Material.valueOf(selectionItemStr);
        } catch(IllegalArgumentException ex) {
            throw new InvalidConfigurationException("Selector item (KoTHs.Area_Selection.Item) is not a valid Material: \"" + selectionItemStr + "\"");
        }
        
        NexGenKoths.selectOnlyInCreative = getConfig().getBoolean("KoTHs.Area_Selection.OnlyInCreative", NexGenKoths.selectOnlyInCreative);
        
        
        NexGenKoths.zoneCaptureCooldown = getConfig().getLong("KoTHs.KoTH_Capture.KoTH_Capture_Cooldown", NexGenKoths.zoneCaptureCooldown);
        NexGenKoths.preventEntryOnCaptureCooldown = getConfig().getBoolean("KoTHs.KoTH_Capture.Prevent_Entry_On_Capture_Cooldown", NexGenKoths.preventEntryOnCaptureCooldown);
        NexGenKoths.zoneCaptureCooldownMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("KoTHs.KoTH_Capture.KoTH_Capture_Cooldown_Message", NexGenKoths.zoneCaptureCooldownMsg));
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
    
    
    protected void performUpdateCheck(boolean download) {
    	Updater updater = new Updater(this, 86133, getFile(), download ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD, false);
    	
    	// The plugin is a development build.
    	if(Updater.hasTag(getDescription().getVersion())) {
    		NexGenKoths.setStaffWarning(
    			ChatColor.translateAlternateColorCodes('&',
    				String.format(
    					"&d" + tag + " &4Warning:\n" +
    					"&d" + tag + " &cYou are running a development build of NexGen KoTHs.\n" +
    					"&d" + tag + " &cThis build will &4&lNOT&c check for updates.\n" +
    					"&d" + tag + " &cMake sure to check for updates at: &ahttp://dev.bukkit.org/bukkit-plugins/nexgen-koths/",
    				updater.getLatestVersion())
    			)
    		);
    	}
    	// An update is available.
    	else if(updater.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE)) {
    		NexGenKoths.setStaffWarning(
    			ChatColor.translateAlternateColorCodes('&',
    				String.format(
    					"&d" + tag + " &6An update is available for NexGen KoTHs&6! &aNew version: %s\n" +
    					"&d" + tag + " &6Download it now at: &chttp://dev.bukkit.org/bukkit-plugins/nexgen-koths/",
    				updater.getLatestVersion())
    			)
    		);
    	}
    	// An update was downloaded.
    	else if(updater.getResult().equals(Updater.UpdateResult.SUCCESS)) {
    		NexGenKoths.setStaffWarning(
    			ChatColor.translateAlternateColorCodes('&',
    				String.format(
    					"&d" + tag + " &6An update has been downloaded for &dNexGen KoTHs&6! &aNew version: %s\n" +
    					"&d" + tag + " &6This update will be installed next restart/reload.\n" +
    					"&d" + tag + " &6Check out the new features here: &chttp://dev.bukkit.org/bukkit-plugins/nexgen-koths/",
    				updater.getLatestVersion())
    			)
    		);
    	}
    }
    
    
    public static void log(Level level, String message) {
        Bukkit.getServer().getLogger().log(level, tag + " " + message);
    }
    
    
    {
        tag = "[" + getDescription().getName() + "]";
        pluginFile = getFile();
    }
}
