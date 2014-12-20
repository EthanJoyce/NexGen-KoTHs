package org.mle.nexgenkoths.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.KothDataHandler;
import org.mle.nexgenkoths.LocationPair;
import org.mle.nexgenkoths.NexGenKoths;

public class SetZoneCmd extends NexGenCmd { // TODO Remove
    
	public SetZoneCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    if(!(sender instanceof Player)) {
	        msg("&cOnly players can execute this command.");
	        return;
	    }
	    Player player = (Player) sender;
	    
	    if(hasArgs(2)) {
	        msg("&cInvalid command arguments.");
	        return;
	    }
	    
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
