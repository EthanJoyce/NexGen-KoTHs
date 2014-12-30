package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollection;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollectionItem;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 1)
public class ItemCollectionContentsCmd extends NexGenCmd {
    
	public ItemCollectionContentsCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    String itemCollectionName = getArg(1);
	    ItemCollection itemCollection = NexGenKoths.getItemCollectionByName(itemCollectionName);
	    
	    if(itemCollection == null) {
	        msg("&cNo ItemCollection with name \"" + itemCollectionName + "\" exists.");
	        return;
	    }
	    
	    StringBuilder contentsList = new StringBuilder("&b&l" + itemCollection.getName() + "'s Contents:\n");
	    
	    for(ItemCollectionItem item : itemCollection.getItems())
	        contentsList.append(String.format(" &dItem: &a%s, &cAmount: &a%s\n", item.getItemStack().getType().toString(), item.getAmountRange().getMin() + "-" + item.getAmountRange().getMax()));
	    
	    msg(contentsList.toString());
	}
    
}
