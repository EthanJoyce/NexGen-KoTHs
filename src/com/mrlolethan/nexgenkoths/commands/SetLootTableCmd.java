package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothDataHandler;
import com.mrlolethan.nexgenkoths.loottables.LootTable;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 2)
public class SetLootTableCmd extends NexGenCmd {
    
	public SetLootTableCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
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
