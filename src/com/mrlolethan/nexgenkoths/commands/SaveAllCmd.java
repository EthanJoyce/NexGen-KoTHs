package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.KothDataHandler;

@Cmd(senderType = CommandSenderType.ANY)
public class SaveAllCmd extends NexGenCmd {
    
	public SaveAllCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
		KothDataHandler.saveAllKoths();
	    
	    msg("&aAll KoTHs were saved successfully!");
	}
    
}
