package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothDataHandler;
import com.mrlolethan.nexgenkoths.util.NumberUtils;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 2)
public class UnsetMessageCmd extends NexGenCmd {
    
	public UnsetMessageCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with the name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    if(!NumberUtils.isLong(getArg(2))) {
            msg("&c" + getArg(2) + " is not a valid number.");
            return;
        }
	    
	    final Long time = Long.valueOf(getArg(2));
	    
	    
	    if(koth.getCapTimeMessage(time) != null) {
	        koth.removeCapTimeMessage(time);
	        KothDataHandler.saveKoth(koth);
	        
	        msg("&aSuccessfully removed the capture time message for the time \"" + time + "\"");
	        return;
	    } else {
	        msg("&cNo capture time message has been set for the time \"" + time + "\"");
	        return;
	    }
	}
    
}
