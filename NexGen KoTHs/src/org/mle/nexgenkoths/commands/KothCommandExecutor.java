package org.mle.nexgenkoths.commands;

import java.util.Map.Entry;

import net.gravitydevelopment.updater.nexgenkoths.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.Koth;
import org.mle.nexgenkoths.KothDataHandler;
import org.mle.nexgenkoths.KothFlag;
import org.mle.nexgenkoths.LocationPair;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.loottables.LootTable;
import org.mle.nexgenkoths.loottables.LootTableDataHandler;
import org.mle.nexgenkoths.loottables.LootTableItem;
import org.mle.nexgenkoths.loottables.NonItemLoot;
import org.mle.nexgenkoths.util.NumberUtils;

public class KothCommandExecutor implements CommandExecutor {
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 0) {
		    switch(args[0].toLowerCase()) {
		    
		    case "help":
		        return onHelpCommand(sender, cmd, label, args);
		    case "create":
		        return onCreateCommand(sender, cmd, label, args);
		    case "wand":
		        return onWandCommand(sender, cmd, label, args);
		    case "list":
		        return onListCommand(sender, cmd, label, args);
		    case "setflag":
		        return onSetFlagCommand(sender, cmd, label, args);
		    case "viewflags":
		        return onViewFlagsCommand(sender, cmd, label, args);
		    case "delete":
		        return onDeleteCommand(sender, cmd, label, args);
		    case "saveall":
		        return onSaveAllCommand(sender, cmd, label, args);
		    case "start":
		        return onStartCommand(sender, cmd, label, args);
		    case "reload":
		        return onReloadCommand(sender, cmd, label, args);
		    case "setloottable":
		        return onSetLootTableCommand(sender, cmd, label, args);
		    case "viewloottable":
		        return onViewLootTableCommand(sender, cmd, label, args);
		    case "listloottables":
		        return onListLootTablesCommand(sender, cmd, label, args);
		    case "loottablecontents":
		        return onLootTableContentsCommand(sender, cmd, label, args);
		    case "version":
		        return onVersionCommand(sender, cmd, label, args);
		    case "viewtimers":
		        return onViewTimersCommand(sender, cmd, label, args);
		    case "update":
		        return onUpdateCommand(sender, cmd, label, args);
		    default:
		        sender.sendMessage(ChatColor.RED + "Unknown Sub-Command. Type \"/" + label + " help\" for help.");
		        return true;
		    
		    }
		} else {
		    sender.sendMessage(ChatColor.RED + "Type \"/" + label + " help\" for help.");
		}
		
		return true;
	}
	
	
	private static boolean onHelpCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.help")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    StringBuilder helpMessage = new StringBuilder(String.format(ChatColor.GOLD + "----- %sNexGen KoTHs Help: %s-----\n", ChatColor.AQUA, ChatColor.GOLD));
	    
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s help %s- Shows this help text.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s wand %s- Gives the player the current selector item.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s list %s- Shows a list of all loaded KoTHs.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s create <Name> %s- Create a KoTH at the selected points.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s setflag <Name> <Flag Name> <Value> %s- Sets the flag value of a KoTH.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s viewflags <Name> %s- View the flags of the specified KoTH.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s delete <Name> %s- Deletes the specified KoTH.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s saveall %s- Saves all KoTHs.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s start %s- Starts the specified KoTH.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s reload %s- Reloads the plugin.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s setloottable <Name> <LootTable Name> %s- Sets the LootTable that the KoTH uses.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s viewloottable <Name> %s- Shows the LootTable that the KoTH uses.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s listloottables %s- Shows a list of the loaded LootTables.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s loottablecontents <Name> %s- Shows the contents of the specified LootTable.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s version %s- Shows the current plugin version.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s viewtimers <Name> %s- Shows the timers on a KoTH.\n", label, ChatColor.RED));
	    helpMessage.append(String.format(ChatColor.GREEN + " /%s update %s- Checks Bukkit Dev for an update.\n", label, ChatColor.RED));
	    
	    helpMessage.append(ChatColor.GOLD + "---------------------------");
	    
	    sender.sendMessage(helpMessage.toString());
	    return true;
	}
	
	
	private static boolean onCreateCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.create")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(!(sender instanceof Player)) {
	        sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
	        return true;
	    }
	    Player player = (Player) sender;
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    LocationPair locPair = NexGenKoths.playerSelections.get(player.getUniqueId());
	    if(locPair == null || locPair.getLocation1() == null || locPair.getLocation2() == null) {
	        sender.sendMessage(ChatColor.RED + "Please make a selection first.");
	        return true;
	    }
	    
	    String name = args[1].replaceAll("[^A-Za-z0-9]", "");
	    
	    if(name.length() > 12) {
	        sender.sendMessage(ChatColor.RED + "That name is too long (" + name.length() + "). Please keep the length under 13 characters.");
	        return true;
	    }
	    
	    for(Koth koth : NexGenKoths.loadedKoths) {
	        if(koth.getName().equalsIgnoreCase(name)) {
	            sender.sendMessage(ChatColor.RED + "A KoTH with the name \"" + name + "\" already exists.");
	            return true;
	        }
	    }
	    
	    Koth koth = new Koth(name, locPair);
	    NexGenKoths.loadedKoths.add(koth);
	    KothDataHandler.saveKoth(koth);
	    
	    sender.sendMessage(ChatColor.GREEN + "Successfully created KoTH \"" + name + "\"");
	    
	    return true;
	}
	
	
	private static boolean onWandCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.wand")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(sender instanceof Player) {
	        Player player = (Player) sender;
	        
	        player.getInventory().addItem(new ItemStack(NexGenKoths.selectionItem, 1));
	    } else {
	        sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
	    }
	    
	    return true;
	}
	
	
	private static boolean onListCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.list")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    StringBuilder listMessage = new StringBuilder(String.format(ChatColor.GOLD + "----- %sLoaded KoTHs: %s-----\n" + ChatColor.GREEN, ChatColor.AQUA, ChatColor.GOLD));
	    
	    for(Koth koth : NexGenKoths.loadedKoths)
	        listMessage.append(String.format(" %s%s%s,", koth.isActive() ? ChatColor.GREEN : ChatColor.RED, koth.getName(), ChatColor.AQUA));
	    
	    listMessage.append(ChatColor.GOLD + "\n-----------------------");
	    
	    sender.sendMessage(listMessage.toString());
	    return true;
	}
	
	
	private static boolean onSetFlagCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.setflag")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 4) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        
	        StringBuilder flagsList = new StringBuilder(ChatColor.AQUA + "Flags:");
	        
	        for(KothFlag f : KothFlag.values())
	            flagsList.append(" " + ChatColor.LIGHT_PURPLE + f.toString().toLowerCase() + ChatColor.GREEN + ",");
	        
	        sender.sendMessage(flagsList.toString());
	        
	        return true;
	    }
	    
	    String kothName = args[1];
	    String flagName = args[2];
	    String flagValue = args[3];
	    
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    KothFlag flag;
	    int value;
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    try {
	        flag = KothFlag.valueOf(flagName.toUpperCase());
	    } catch(IllegalArgumentException ex) {
	        sender.sendMessage(ChatColor.RED + "Unknown flag \"" + flagName + "\"");
	        
	        StringBuilder flagsList = new StringBuilder(ChatColor.AQUA + "Flags:" + ChatColor.LIGHT_PURPLE);
	        
	        for(KothFlag f : KothFlag.values())
	            flagsList.append(" " + f.toString().toLowerCase() + ",");
	        
	        sender.sendMessage(flagsList.toString());
	        return true;
	    }
	    
	    if(!flagValue.equalsIgnoreCase("false") && !flagValue.equalsIgnoreCase("true") && !NumberUtils.isInteger(flagValue)) {
	        sender.sendMessage(ChatColor.RED + "Invalid flag value \"" + flagValue + "\"");
	        return true;
	    }
	    
	    if(flagValue.equalsIgnoreCase("true"))
	        value = 1;
	    else if(flagValue.equalsIgnoreCase("false"))
	        value = 0;
	    else
	        value = Integer.parseInt(flagValue);
	    
	    
	    koth.getFlags().put(flag, value);
	    
	    sender.sendMessage(ChatColor.GREEN + "Successfully set flag \"" + flag.toString().toLowerCase() + "\" to \"" + flagValue + "\" for KoTH \"" + koth.getName() + "\"");
	    
	    KothDataHandler.saveKoth(koth);
	    return true;
	}
	
	
	private static boolean onViewFlagsCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.viewflags")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String kothName = args[1];
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    StringBuilder flagsList = new StringBuilder(ChatColor.AQUA.toString() + ChatColor.BOLD + koth.getName() + "'s Flags:\n");
	    
	    for(Entry<KothFlag, Integer> flag : koth.getFlags().entrySet())
	        flagsList.append(String.format(" %s: %s\n", ChatColor.GREEN + flag.getKey().toString().toLowerCase(), ChatColor.RED + flag.getValue().toString()));
	    
	    sender.sendMessage(flagsList.toString());
	    return true;
	}
	
	
	private static boolean onDeleteCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.delete")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String kothName = args[1];
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    KothDataHandler.deleteKoth(koth);
	    
	    sender.sendMessage(ChatColor.GREEN + "KoTH \"" + koth.getName() + "\" was deleted successfully!");
	    return true;
	}
	
	
	private static boolean onSaveAllCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.saveall")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    KothDataHandler.saveAllKoths();
	    
	    sender.sendMessage(ChatColor.GREEN + "All KoTHs were saved successfully!");
	    return true;
	}
	
	
	private static boolean onStartCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.start")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String kothName = args[1];
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    if(!koth.isActive())
	        koth.startKoth();
	    else {
	        sender.sendMessage(ChatColor.RED + "That KoTH is already active.");
	        return true;
	    }
	    
	    sender.sendMessage(ChatColor.GREEN + "Successfully started KoTH \"" + koth.getName() + "\"");
	    return true;
	}
	
	
	private static boolean onReloadCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.reload")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    LootTableDataHandler.loadAllLootTables();
	    
	    NexGenKoths.instance.reloadConfig();
	    
	    try {
	        NexGenKoths.instance.loadConfiguration();
	    } catch(InvalidConfigurationException ex) {
            Bukkit.getLogger().severe(NexGenKoths.tag + " Error loading config: " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(NexGenKoths.instance);
        }
	    
	    sender.sendMessage(ChatColor.GREEN + "Successfully reloaded plugin.");
	    return true;
	}
	
	
	private static boolean onSetLootTableCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.setloottable")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 3) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String kothName = args[1];
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    String lootTableName = args[2];
	    LootTable lootTable = NexGenKoths.getLootTableByName(lootTableName);
	    
	    if(lootTable == null) {
	        sender.sendMessage(ChatColor.RED + "No LootTable with name \"" + lootTableName + "\" exists.");
	        return true;
	    }
	    
	    koth.setLootTable(lootTable);
	    
	    sender.sendMessage(ChatColor.GREEN + "Successfully set the LootTable of KoTH \"" + koth.getName() + "\" to \"" + lootTable.getName() + "\"");
	    KothDataHandler.saveKoth(koth);
	    return true;
	}
	
	
	private static boolean onViewLootTableCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.viewloottable")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String kothName = args[1];
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    if(koth.getLootTable() == null) {
	        sender.sendMessage(ChatColor.RED + "That KoTH doesn't have an assigned loot table.");
	        return true;
	    }
	    
	    
	    sender.sendMessage(ChatColor.GREEN + koth.getName() + "'s LootTable: " + ChatColor.RED + koth.getLootTable().getName());
	    
	    return true;
	}
	
	
	private static boolean onListLootTablesCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.listloottables")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    StringBuilder listMessage = new StringBuilder(String.format(ChatColor.GOLD + "----- %sLoaded LootTables: %s-----\n" + ChatColor.GREEN, ChatColor.AQUA, ChatColor.GOLD));
	    
	    for(LootTable lootTable : NexGenKoths.loadedLootTables)
	        listMessage.append(" " + lootTable.getName() + ",");
	    
	    listMessage.append(ChatColor.GOLD + "\n---------------------------");
	    
	    sender.sendMessage(listMessage.toString());
	    
	    return true;
	}
	
	
	private static boolean onLootTableContentsCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.loottablecontents")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String lootTableName = args[1];
	    LootTable lootTable = NexGenKoths.getLootTableByName(lootTableName);
	    
	    if(lootTable == null) {
	        sender.sendMessage(ChatColor.RED + "No LootTable with name \"" + lootTableName + "\" exists.");
	        return true;
	    }
	    
	    StringBuilder contentsList = new StringBuilder(ChatColor.AQUA.toString() + ChatColor.BOLD + lootTable.getName() + "'s Contents:\n");
	    
	    for(LootTableItem item : lootTable.getItems())
	        contentsList.append(String.format(" %sItem: %s, %sAmount: %s, %sChance: %s\n", ChatColor.LIGHT_PURPLE, ChatColor.GREEN + item.getItemStack().getType().toString(), ChatColor.RED, ChatColor.GREEN.toString() + item.getAmountRange().getMin() + "-" + item.getAmountRange().getMax(), ChatColor.BLUE, ChatColor.GREEN.toString() + item.getChance()));
	    
	    contentsList.append(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Non-Item Loot:\n");
	    
	    for(NonItemLoot loot : lootTable.getNonItemLootList())
	        contentsList.append(String.format(" %sName: %s, %sAmount: %s, %sChance: %s\n", ChatColor.LIGHT_PURPLE, ChatColor.GREEN + loot.getName(), ChatColor.RED, ChatColor.GREEN.toString() + loot.getAmountRange().getMin() + "-" + loot.getAmountRange().getMax(), ChatColor.BLUE, ChatColor.GREEN.toString() + loot.getChance()));
	    
	    sender.sendMessage(contentsList.toString());
	    return true;
	}
	
	
	private static boolean onVersionCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.version")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    Updater updater  = new Updater(NexGenKoths.instance, 86133, NexGenKoths.pluginFile, Updater.UpdateType.NO_DOWNLOAD, false);
	    boolean isUpToDate = updater.getLatestVersion().equals(NexGenKoths.instance.getDescription().getVersion());
	    
	    sender.sendMessage(ChatColor.GREEN + "Version: " + NexGenKoths.instance.getDescription().getVersion() + ChatColor.GOLD +  ", " + (isUpToDate ? ChatColor.GREEN : ChatColor.RED) + "Latest Version: " + updater.getLatestVersion());
	    
	    return true;
	}
	
	
	private static boolean onViewTimersCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.viewtimers")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    if(args.length != 2) {
	        sender.sendMessage(ChatColor.RED + "Invalid command arguments.");
	        return true;
	    }
	    
	    String kothName = args[1];
	    Koth koth = NexGenKoths.getKothByName(kothName);
	    
	    if(koth == null) {
	        sender.sendMessage(ChatColor.RED + "No KoTH with name \"" + kothName + "\" exists.");
	        return true;
	    }
	    
	    
	    sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + koth.getName() + "'s Timers: ");
	    sender.sendMessage(ChatColor.GREEN + " Auto Start: " + ChatColor.RED + koth.getAutoStartTimer());
	    sender.sendMessage(ChatColor.GREEN + " Auto End: " + ChatColor.RED + koth.getAutoEndTimer());
	    
	    return true;
	}
	
	
	private static boolean onUpdateCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!sender.hasPermission("nexgenkoths.update")) {
	        sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
	        return true;
	    }
	    
	    Updater updater  = new Updater(NexGenKoths.instance, 86133, NexGenKoths.pluginFile, Updater.UpdateType.DEFAULT, false);
	    
	    switch(updater.getResult()) {
	    
	    case NO_UPDATE:
	        sender.sendMessage(ChatColor.GREEN + "No update was found.");
	        return true;
	    case SUCCESS:
	        sender.sendMessage(ChatColor.GREEN + "An update was found and will be installed next restart/reload. New version: " + updater.getLatestVersion());
	        return true;
	    case UPDATE_AVAILABLE:
	        sender.sendMessage(ChatColor.GREEN + "An update is available! Download it at \"" + updater.getLatestFileLink() + "\". New version: " + updater.getLatestVersion());
	        return true;
	    case FAIL_DOWNLOAD:
	        sender.sendMessage(ChatColor.RED + "An update was found, but wasn't downloaded successfully. New version: " + updater.getLatestVersion());
	        return true;
	    case DISABLED:
	        sender.sendMessage(ChatColor.RED + "The updater for this server is disabled.");
	        return true;
	    default:
	        sender.sendMessage(ChatColor.RED + "Unexpected update result: " + updater.getResult().toString());
	        return true;
	    
	    }
	}
    
    
}
