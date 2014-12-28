package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.koth.Koth;

public class StopCmd extends NexGenCmd {
    
	public StopCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
		if(!hasArgs(2)) {
	        msg("&cInvalid command arguments.");
	        return;
	    }
	    
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    if(koth.isActive()) {
	        koth.stopKoth(true);
	    } else {
	        msg("&cThat KoTH is not active.");
	        return;
	    }
	    
	    msg("&aSuccessfully stopped KoTH \"" + koth.getName() + "\"");
	}
    
}
