package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.Permissions;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCommand;

@Cmd(senderType = CommandSenderType.ANY)
public class HelpCmd extends NexGenCmd {
    
	public HelpCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
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
