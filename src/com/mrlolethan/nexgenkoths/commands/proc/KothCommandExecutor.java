package com.mrlolethan.nexgenkoths.commands.proc;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
		    Constructor<?> cmdCtor = cmdClass.getConstructor(CommandSender.class, Command.class, String.class, String.class, String[].class);
		    
		    Cmd cmdAnno = cmdClass.getAnnotation(Cmd.class);
		    if(cmdAnno == null) {
		    	sender.sendMessage(ChatColor.RED + "Error performing command: the specified sub-command is not annotated properly.");
		    	return true;
			} else {
				switch(cmdAnno.senderType()) {
				
				case PLAYER:
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.RED + "Only players can perform this command.");
						return true;
					}
					break;
				case CONSOLE:
					if(!(sender instanceof ConsoleCommandSender)) {
						sender.sendMessage(ChatColor.RED + "Only the console can perform this command.");
						return true;
					}
					break;
				case ANY:
					break;
				
				}
				
				if((args.length - 1) < cmdAnno.argsRequired() && cmdAnno.argsRequired() != 0) {
	        		sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        		return true;
	    		}
			}
		    
		    ngcmd = (NexGenCmd) cmdCtor.newInstance(new Object [] { sender, cmd, command.getCmd(), label, args });
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
