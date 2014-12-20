package org.mle.nexgenkoths.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.NexGenKoths;

public class ListCmd extends NexGenCmd {
    
	public ListCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
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
