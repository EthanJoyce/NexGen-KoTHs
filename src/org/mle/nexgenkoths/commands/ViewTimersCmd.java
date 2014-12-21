package org.mle.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.NexGenKoths;

public class ViewTimersCmd extends NexGenCmd { // TODO Remove
    
	public ViewTimersCmd(CommandSender sender, Command cmd, String label, String[] args) {
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
	    
	    
	    msg("&b&l" + koth.getName() + "'s Timers: ");
	    msg(" &aAuto Start: &c" + koth.getAutoStartTimer());
	    msg(" &aAuto End: &c" + koth.getAutoEndTimer());
	    if(koth.isBeingCaptured()) msg(" &aCapture: &c" + koth.getCaptureTimer());
	}
    
}
