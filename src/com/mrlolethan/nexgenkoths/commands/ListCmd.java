package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;

@Cmd(senderType = CommandSenderType.ANY)
public class ListCmd extends NexGenCmd {
    
	public ListCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
		StringBuilder listMessage = new StringBuilder("&6----- &bLoaded KoTHs: &6-----\n&a");
	    
	    for(Koth koth : NexGenKoths.loadedKoths)
	        listMessage.append(String.format(" %s%s&b,", koth.isActive() ? "&a" : "&c", koth.getName()));
	    
	    listMessage.append(ChatColor.GOLD + "\n-----------------------");
	    
	    msg(listMessage.toString());
	}
    
}
