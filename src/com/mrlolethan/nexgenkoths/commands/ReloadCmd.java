package com.mrlolethan.nexgenkoths.commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import com.mrlolethan.nexgenkoths.P;
import com.mrlolethan.nexgenkoths.customitems.CustomItemsDataHandler;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollectionDataHandler;
import com.mrlolethan.nexgenkoths.loottables.LootTableDataHandler;

public class ReloadCmd extends NexGenCmd {
    
	public ReloadCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
		CustomItemsDataHandler.loadAllCustomItems();
	    ItemCollectionDataHandler.loadAllItemCollections();
	    LootTableDataHandler.loadAllLootTables();
	    
	    P.p.reloadConfig();
	    
	    P.p.initConfiguration();
	    try {
	        P.p.loadConfiguration();
	    } catch(InvalidConfigurationException ex) {
            P.log(Level.SEVERE, "Error loading config: " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(P.p);
        }
	    
	    msg("&aSuccessfully reloaded plugin.");
	}
    
}
