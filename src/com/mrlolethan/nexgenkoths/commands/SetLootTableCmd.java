package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.Koth;
import com.mrlolethan.nexgenkoths.KothDataHandler;
import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.loottables.LootTable;

public class SetLootTableCmd extends NexGenCmd { // TODO Remove
    
	public SetLootTableCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    if(!hasArgs(3)) {
	        msg("&cInvalid command arguments.");
	        return;
	    }
	    
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    String lootTableName = getArg(2);
	    LootTable lootTable = NexGenKoths.getLootTableByName(lootTableName);
	    
	    if(lootTable == null) {
	        msg("&cNo LootTable with name \"" + lootTableName + "\" exists.");
	        return ;
	    }
	    
	    koth.setLootTable(lootTable);
	    
	    msg("&aSuccessfully set the LootTable of KoTH \"" + koth.getName() + "\" to \"" + lootTable.getName() + "\"");
	    KothDataHandler.saveKoth(koth);
	}
    
}
