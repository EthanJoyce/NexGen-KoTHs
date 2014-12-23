package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollection;

public class ListItemCollectionsCmd extends NexGenCmd { // TODO Remove
    
	public ListItemCollectionsCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    StringBuilder listMessage = new StringBuilder("&6----- &bLoaded ItemCollections: &6-----\n&a");
	    
	    for(ItemCollection itemCollection : NexGenKoths.loadedItemCollections)
	        listMessage.append(" " + itemCollection.getName() + ",");
	    
	    listMessage.append("&6\n------------------------------");
	    
	    msg(listMessage.toString());
	}
    
}
