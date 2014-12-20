package org.mle.nexgenkoths.commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.mle.nexgenkoths.P;
import org.mle.nexgenkoths.customitems.CustomItemsDataHandler;
import org.mle.nexgenkoths.itemcollections.ItemCollectionDataHandler;
import org.mle.nexgenkoths.loottables.LootTableDataHandler;

public class ReloadCmd extends NexGenCmd { // TODO Remove
    
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
