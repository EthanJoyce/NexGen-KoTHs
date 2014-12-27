package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.Koth;
import com.mrlolethan.nexgenkoths.NexGenKoths;

public class ViewLootTableCmd extends NexGenCmd {
    
	public ViewLootTableCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    if(!hasArgs(2)) {
	        msg("&cInvalid command arguments.");
	        return;
	    }
	    
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    if(koth.getLootTable() == null) {
	        msg("&cThat KoTH doesn't have an assigned loot table.");
	        return;
	    }
	    
	    msg("&a" + koth.getName() + "'s LootTable: &c" + koth.getLootTable().getName());
	}
    
}
