package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mrlolethan.nexgenkoths.NexGenKoths;

public class WandCmd extends NexGenCmd {
    
	public WandCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
		if(sender instanceof Player)
	        ((Player) sender).getInventory().addItem(new ItemStack(NexGenKoths.selectionItem, 1));
	    else
	        msg("&cOnly players can execute this command.");
	}
    
}
