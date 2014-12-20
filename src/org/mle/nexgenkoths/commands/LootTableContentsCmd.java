package org.mle.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.loottables.LootTable;
import org.mle.nexgenkoths.loottables.LootTableItem;
import org.mle.nexgenkoths.loottables.NonItemLoot;

public class LootTableContentsCmd extends NexGenCmd { // TODO Remove
    
	public LootTableContentsCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    if(!hasArgs(2)) {
	        msg("&cInvalid command arguments.");
	        return;
	    }
	    
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
