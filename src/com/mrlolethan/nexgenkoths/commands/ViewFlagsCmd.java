package com.mrlolethan.nexgenkoths.commands;

import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothFlag;

public class ViewFlagsCmd extends NexGenCmd {
    
	public ViewFlagsCmd(CommandSender sender, Command cmd, String label, String[] args) {
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
	    
	    StringBuilder flagsList = new StringBuilder("&b&l" + koth.getName() + "'s Flags:\n");
	    
	    for(Entry<KothFlag, Integer> flag : koth.getFlags().entrySet())
	        flagsList.append(String.format(" &a%s: &c%s\n", flag.getKey().toString().toLowerCase(), flag.getValue().toString()));
	    
	    msg(flagsList.toString());
	}
    
}
