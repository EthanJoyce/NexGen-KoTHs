package org.mle.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.loottables.LootTable;

public class ListLootTablesCmd extends NexGenCmd { // TODO Remove
    
	public ListLootTablesCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    StringBuilder listMessage = new StringBuilder("&6----- &bLoaded LootTables: &6-----\n" + "&c");
	    
	    for(LootTable lootTable : NexGenKoths.loadedLootTables)
	        listMessage.append(" " + lootTable.getName() + ",");
	    
	    listMessage.append("&6\n---------------------------");
	    
	    msg(listMessage.toString());
	}
    
}
