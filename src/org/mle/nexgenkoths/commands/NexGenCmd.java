package org.mle.nexgenkoths.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class NexGenCmd {
    
    public CommandSender sender;
    public boolean isSenderPlayer;
    
    public Command cmd;
    public String label;
    private String[] args;
    
    public NexGenCmd(CommandSender sender, Command cmd, String label, String[] args) {
		this.sender = sender;
		this.isSenderPlayer = (sender instanceof Player);
		
		this.cmd = cmd;
		this.label = label;
		this.args = args;
	}
	
	public boolean hasPermission(String perm) {
	    return sender.hasPermission(perm);
	}
	
	public void msg(String message) {
	    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	
	public boolean hasArgs(int amt) {
	    return args.length >= amt;
	}
	
	public String getArg(int i) {
	    if((args.length - 1) >= i)
	        return args[i];
	    else
	        return null;
	}
	
	public String[] getArgs() {
	    return args;
	}
	
	
	public abstract void perform();
    
    
}
