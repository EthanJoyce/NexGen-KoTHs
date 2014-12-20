package org.mle.nexgenkoths.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.KothDataHandler;
import org.mle.nexgenkoths.LocationPair;
import org.mle.nexgenkoths.NexGenKoths;

public class CreateCmd extends NexGenCmd {
    
	public CreateCmd(CommandSender sender, Command cmd, String label, String[] args) {
		super(sender, cmd, label, args);
	}
    
    
	@Override
	public void perform() {
	    if(!(sender instanceof Player)) {
	        msg("&cOnly players can perform this command.");
	        return;
	    }
	    Player player = (Player) sender;
	    
	    if(!hasArgs(2)) {
	        msg("&cInvalid command arguments.");
	        return;
	    }
	    
	    LocationPair locPair = NexGenKoths.playerSelections.get(player.getUniqueId());
	    if(locPair == null || locPair.getLocation1() == null || locPair.getLocation2() == null) {
	        msg("&cPlease make a selection first.");
	        return;
	    }
	    
	    String name = getArg(1).replaceAll("[^A-Za-z0-9]", "");
	    
	    if(name.length() > 11) {
	        msg("&cThat name is too long (" + name.length() + "). Please keep the length under 12 characters.");
	        return;
	    }
	    
	    if(NexGenKoths.getKothByName(name) != null) {
	        msg("&cA KoTH with the name \"" + name + "\" already exists.");
	        return;
	    }
	    
	    Map<Long, String> capTimeMessages = new HashMap<Long, String>();
	    capTimeMessages.put(60L, ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + " has " + ChatColor.GREEN + "{TIME_LEFT}" + ChatColor.GOLD + " seconds left until it is captured!");
	    capTimeMessages.put(30L, ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + " has " + ChatColor.GREEN + "{TIME_LEFT}" + ChatColor.GOLD + " seconds left until it is captured!");
	    capTimeMessages.put(15L, ChatColor.LIGHT_PURPLE + "[KoTH] " + ChatColor.GOLD + ChatColor.BOLD + "{KOTH_NAME}" + ChatColor.GOLD + " has " + ChatColor.GREEN + "{TIME_LEFT}" + ChatColor.GOLD + " seconds left until it is captured!");
	    
	    Koth koth = new Koth(name, locPair);
	    koth.setCapTimeMessages(capTimeMessages);
	    
	    NexGenKoths.loadedKoths.add(koth);
	    KothDataHandler.saveKoth(koth);
	    
	    msg("&aSuccessfully created KoTH \"" + name + "\"");
	}
    
}
