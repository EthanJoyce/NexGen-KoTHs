package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.loottables.LootTable;
import com.mrlolethan.nexgenkoths.loottables.LootTableItem;
import com.mrlolethan.nexgenkoths.loottables.NonItemLoot;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 1)
public class LootTableContentsCmd extends NexGenCmd {
    
	public LootTableContentsCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    String lootTableName = getArg(1);
	    LootTable lootTable = NexGenKoths.getLootTableByName(lootTableName);
	    
	    if(lootTable == null) {
	        msg("&cNo LootTable with name \"" + lootTableName + "\" exists.");
	        return;
	    }
	    
	    StringBuilder contentsList = new StringBuilder("&b&l" + lootTable.getName() + "'s Contents:\n");
	    
	    for(LootTableItem item : lootTable.getItems())
	        contentsList.append(String.format(" &dItem: &a%s, &cAmount: &a%s, &9Chance: &a%s\n", item.getItemStack().getType().toString(), item.getAmountRange().getMin() + "-" + item.getAmountRange().getMax(), item.getChance()));
	    
	    contentsList.append("&d&lNon-Item Loot:\n");
	    
	    for(NonItemLoot loot : lootTable.getNonItemLootList())
	        contentsList.append(String.format(" &dName: &a%s, &cAmount: &a%s, &9Chance: &a%s\n", loot.getName(), loot.getAmountRange().getMin() + "-" + loot.getAmountRange().getMax(), loot.getChance()));
	    
	    msg(contentsList.toString());
	}
    
}
