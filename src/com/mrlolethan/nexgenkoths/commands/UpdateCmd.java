package com.mrlolethan.nexgenkoths.commands;

import net.gravitydevelopment.updater.nexgenkoths.Updater;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.P;

public class UpdateCmd extends NexGenCmd {
    
	public UpdateCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    Updater updater  = new Updater(P.p, 86133, P.pluginFile, Updater.UpdateType.DEFAULT, true);
	    
	    switch(updater.getResult()) {
	    
	    case NO_UPDATE:
	        msg("&aNo update was found.");
	        return;
	    case SUCCESS:
	        msg("&aAn update was found and will be installed next restart/reload. New version: " + updater.getLatestVersion());
	        return;
	    case UPDATE_AVAILABLE:
	        msg("&aAn update is available! Download it at \"" + updater.getLatestFileLink() + "\". New version: " + updater.getLatestVersion());
	        return;
	    case FAIL_DOWNLOAD:
	        msg("&cAn update was found, but wasn't downloaded successfully. New version: " + updater.getLatestVersion());
	        return;
	    case DISABLED:
	        msg("&cThe updater for this server is disabled.");
	        return;
	    default:
	        msg("&cUnexpected update result: " + updater.getResult().toString());
	        return;
	    
	    }
	}
    
}
