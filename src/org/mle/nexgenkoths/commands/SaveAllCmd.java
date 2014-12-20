package org.mle.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mle.nexgenkoths.KothDataHandler;

public class SaveAllCmd extends NexGenCmd {
    
	public SaveAllCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
		KothDataHandler.saveAllKoths();
	    
	    msg("&aAll KoTHs were saved successfully!");
	}
    
}
