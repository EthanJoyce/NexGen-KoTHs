package com.mrlolethan.nexgenkoths.commands.proc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class NexGenCmd {
    
    public CommandSender sender;
    public Command cmd;
    public String label;
    public String cmdName;
    private String[] args;
    
    public NexGenCmd(CommandSender sender, Command cmd, String cmdName, String label, String[] args) {
		this.sender = sender;
		this.cmdName = cmdName;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
	}
	
	public void msg(String message) {
	    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	
	public boolean hasArgs(int amt) {
	    return (args.length + 1) >= amt;
	}
	
	public String getArg(int i) {
	    if(args.length >= i)
	        return args[i];
	    else
	        return null;
	}
	
	public String[] getArgs() {
	    return args;
	}
	
	
	public abstract void perform();
	
	
}
