package com.mrlolethan.nexgenkoths.commands;

import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;

@Cmd(senderType = CommandSenderType.ANY, argsRequired = 1)
public class ViewMessagesCmd extends NexGenCmd {
    
	public ViewMessagesCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with the name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    StringBuilder messagesList = new StringBuilder("&b&l" + koth.getName() + "'s Messages:\n");
	    
	    for(Entry<Long, String> entry : koth.getCapTimeMessages().entrySet())
	        messagesList.append(String.format(" &dBroadcast Time: &a%s, &cMessage: &r%s\n", entry.getKey(), entry.getValue()));
	    
	    msg(messagesList.toString());
	}
    
}
