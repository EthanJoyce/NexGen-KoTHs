package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothDataHandler;
import com.mrlolethan.nexgenkoths.util.NumberUtils;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 3)
public class SetMessageCmd extends NexGenCmd {
    
	public SetMessageCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
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
