package org.mle.nexgenkoths.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.KothDataHandler;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.util.NumberUtils;

public class SetMessageCmd extends NexGenCmd { // TODO Remove
    
	public SetMessageCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    if(!hasArgs(4)) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return;
	    }
	    
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    if(!NumberUtils.isLong(getArg(2))) {
            msg("&c" + getArg(2) + " is not a valid number.");
            return;
        }
	    
	    StringBuffer message = new StringBuffer(getArg(3));
	    for(int i = 4; i < getArgs().length; i++)
	        message.append(" " + getArg(i));
	    
	    koth.addCapTimeMessage(Long.parseLong(getArg(2)), ChatColor.translateAlternateColorCodes('&', message.toString()));
	    KothDataHandler.saveKoth(koth);
	    
	    msg("&aThe message to be broadcast when the KoTH \"" + koth.getName() + "\" has " + getArg(2) + " seconds left until capture is now: &r" + ChatColor.translateAlternateColorCodes('&', message.toString()));
	}
    
}
