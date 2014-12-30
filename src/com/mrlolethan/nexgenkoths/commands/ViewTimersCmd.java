package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 1)
public class ViewTimersCmd extends NexGenCmd {
    
	public ViewTimersCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
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
	    
	    
	    msg("&b&l" + koth.getName() + "'s Timers: ");
	    msg(" &aAuto Start: &c" + koth.getAutoStartTimer());
	    msg(" &aAuto End: &c" + koth.getAutoEndTimer());
	    if(koth.isBeingCaptured()) msg(" &aCapture: &c" + koth.getCaptureTimer());
	}
    
}
