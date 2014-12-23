package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.Koth;
import com.mrlolethan.nexgenkoths.KothDataHandler;
import com.mrlolethan.nexgenkoths.NexGenKoths;

public class DeleteCmd extends NexGenCmd {
    
	public DeleteCmd(CommandSender sender, Command cmd, String label, String[] args) {
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
	    
	    KothDataHandler.deleteKoth(koth);
	    
	    msg("&aKoTH \"" + koth.getName() + "\" was deleted successfully!");
	}
    
}
