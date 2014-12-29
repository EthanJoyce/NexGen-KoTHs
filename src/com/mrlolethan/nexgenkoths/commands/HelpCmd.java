package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.Permissions;

public class HelpCmd extends NexGenCmd {
    
	public HelpCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    StringBuilder helpMessage = new StringBuilder("&6----- &bNexGen KoTHs Help: &6-----\n");
	    
	    for(NexGenCommand command : NexGenCommand.values())
	        if(Permissions.performCommand(sender, command.getCmd()))
	            helpMessage.append(String.format("&d /%s &a%s &7%s&a &c- %s\n", label, command.getCmd(), command.getArguments(), command.getDescription()));
	    
	    helpMessage.append("&6---------------------------");
	    
	    msg(helpMessage.toString());
	}
	
    
}
