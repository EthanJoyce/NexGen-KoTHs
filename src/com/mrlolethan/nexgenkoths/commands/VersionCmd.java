package com.mrlolethan.nexgenkoths.commands;

import net.gravitydevelopment.updater.nexgenkoths.Updater;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.P;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;

@Cmd(senderType = CommandSenderType.ANY)
public class VersionCmd extends NexGenCmd {
    
	public VersionCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    Updater updater  = new Updater(P.p, 86133, P.pluginFile, Updater.UpdateType.NO_DOWNLOAD, false);
	    boolean isUpToDate = updater.getLatestVersion().equals(P.p.getDescription().getVersion());
	    
	    msg(String.format("&dNexGen KoTHs &av%s, &6Authored by &e&lMrLolEthan", P.p.getDescription().getVersion()));
	    msg("&aVersion: " + P.p.getDescription().getVersion() + "&6, " + (isUpToDate ? "&a" : "&c") + "Latest Version: " + updater.getLatestVersion());
	}
    
}
