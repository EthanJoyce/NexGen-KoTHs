package com.mrlolethan.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.commands.proc.Cmd;
import com.mrlolethan.nexgenkoths.commands.proc.CommandSenderType;
import com.mrlolethan.nexgenkoths.commands.proc.NexGenCmd;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothDataHandler;
import com.mrlolethan.nexgenkoths.objects.LocationPair;

@Cmd(senderType = CommandSenderType.PLAYER, argsRequired = 1)
public class SetZoneCmd extends NexGenCmd {
    
	public SetZoneCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		super(sender, cmd, cmdName, label, args);
	}
    
    
	@Override
	public void perform() {
	    Player player = (Player) sender;
	    
	    LocationPair locPair = NexGenKoths.playerSelections.get(player.getUniqueId());
	    if(locPair == null || locPair.getLocation1() == null || locPair.getLocation2() == null) {
	        msg("&cPlease make a selection first.");
	        return;
	    }
	    
	    
	    String kothName = getArg(1);
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        msg("&cNo KoTH with the name \"" + kothName + "\" exists.");
	        return;
	    }
	    
	    koth.setCapZoneLocations(locPair);
	    KothDataHandler.saveKoth(koth);
	    
	    msg("&aSuccessfully set the capture zone of KoTH \"" + kothName + "\"");
	}
    
}
