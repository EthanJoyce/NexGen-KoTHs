package com.mrlolethan.nexgenkoths.commands;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.P;
import com.mrlolethan.nexgenkoths.Permissions;

public class KothCommandExecutor implements CommandExecutor {
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		NexGenCommand command = NexGenCommand.getCommand(args.length > 0 ? args[0] : "help");
		
		if(command == null) {
		    sender.sendMessage(ChatColor.RED + "Unknown Sub-Command. Type /" + label + " help.");
		    return true;
		}
		
		if(!Permissions.performCommand(sender, command.getCmd())) {
		    sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
		    return true;
		}
		
		Class<? extends NexGenCmd> cmdClass = command.getCmdClass();
		
		if(cmdClass == null) {
		    sender.sendMessage(ChatColor.RED + "Error performing command.");
		    return true;
		}
		
		NexGenCmd ngcmd = null;
		try {
		    Constructor<?> cmdCtor = cmdClass.getConstructor(CommandSender.class, Command.class, String.class, String[].class);
		    
		    ngcmd = (NexGenCmd) cmdCtor.newInstance(new Object [] { sender, cmd, label, args });
		} catch(Exception ex) {
		    ex.printStackTrace();
		    P.log(Level.SEVERE, String.format("Error executing command \"%s\". Args: \"%s\", Sender: \"%s\"", cmd.getName(), Arrays.asList(args), sender.getName()));
		    sender.sendMessage(ChatColor.RED + "Error performing command.");
		    return true;
		}
		
		if(ngcmd == null) {
		    sender.sendMessage(ChatColor.RED + "Error performing command.");
		    return true;
		}
		
		ngcmd.perform();
		return true;
	}
	
    
}
