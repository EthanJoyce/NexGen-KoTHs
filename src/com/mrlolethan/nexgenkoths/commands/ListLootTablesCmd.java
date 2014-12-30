package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.loottables.LootTable;

@Cmd(senderType = CommandSenderType.ANY)
public class ListLootTablesCmd extends NexGenCmd {
    
	public ListLootTablesCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
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
