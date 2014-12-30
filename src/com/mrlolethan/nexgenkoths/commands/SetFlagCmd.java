package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothDataHandler;
import com.mrlolethan.nexgenkoths.koth.KothFlag;
import com.mrlolethan.nexgenkoths.util.NumberUtils;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 3)
public class SetFlagCmd extends NexGenCmd {
    
	public SetFlagCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    // TODO method of listing available flags.
	    /*if(!hasArgs(4)) {
	        msg("&cInvalid command arguments.");
	        
	        StringBuilder flagsList = new StringBuilder("&bFlags:");
	        
	        for(KothFlag f : KothFlag.values())
	            flagsList.append(" &d" + f.toString().toLowerCase() + "&a,");
	        
	        msg(flagsList.toString());
	        return;
	    }*/
	    
	    String kothName = getArg(1);
	    String flagName = getArg(2);
	    String flagValue = getArg(3);
	    
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    KothFlag flag;
	    int value;
	    
	    if(koth == null) {
	        msg("&cNo KoTH with name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    try {
	        flag = KothFlag.valueOf(flagName.toUpperCase());
	    } catch(IllegalArgumentException ex) {
	        msg("&cUnknown flag \"" + flagName + "\"");
	        
	        StringBuilder flagsList = new StringBuilder("&bFlags:&d");
	        
	        for(KothFlag f : KothFlag.values())
	            flagsList.append(" " + f.toString().toLowerCase() + ",");
	        
	        msg(flagsList.toString());
	        return;
	    }
	    
	    if(!flagValue.equalsIgnoreCase("false") && !flagValue.equalsIgnoreCase("true") && !NumberUtils.isInteger(flagValue)) {
	        msg("&cInvalid flag value \"" + flagValue + "\"");
	        return;
	    }
	    
	    if(flagValue.equalsIgnoreCase("true"))
	        value = 1;
	    else if(flagValue.equalsIgnoreCase("false"))
	        value = 0;
	    else
	        value = Integer.parseInt(flagValue);
	    
	    
	    koth.getFlags().put(flag, value);
	    
	    msg("&aSuccessfully set flag \"" + flag.toString().toLowerCase() + "\" to \"" + flagValue + "\" for KoTH \"" + koth.getName() + "\"");
	    
	    KothDataHandler.saveKoth(koth);
	}
    
}
