package org.mle.nexgenkoths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.mle.nexgenkoths.loottables.LootTable;
import org.mle.nexgenkoths.util.NumberUtils;

public enum KothDataHandler {;
    
    private static File dataDir = new File(NexGenKoths.instance.getDataFolder(), "KoTHs");
    
    
    public static void initDirectories() {
        dataDir.mkdirs();
    }
    
    
    public static void saveAllKoths() {
        for(Koth koth : NexGenKoths.loadedKoths)
            saveKoth(koth);
    }
    
    
    public static void saveKoth(Koth koth) {
        File file = new File(dataDir, koth.getName());
        
        if(file.exists()) {
            if(!file.delete()) {
                Bukkit.getLogger().warning(NexGenKoths.tag + " Couldn't delete file \"" + file.getAbsolutePath() + "\"");
                Bukkit.getLogger().warning(NexGenKoths.tag + " Aborting the save of KoTH \"" + koth.getName() + "\"");
                return;
            }
        }
        
        try {
            if(!file.createNewFile()) {
                Bukkit.getLogger().warning(NexGenKoths.tag + " Couldn't create file \"" + file.getAbsolutePath() + "\"");
                Bukkit.getLogger().warning(NexGenKoths.tag + " Aborting the save of KoTH \"" + koth.getName() + "\"");
                return;
            }
        } catch(IOException ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " IOException thrown while creating file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
        
        
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.append("name=" + koth.getName());
            
            out.newLine();
            
            Location l1 = koth.getCapZoneLocations().getLocation1();
            Location l2 = koth.getCapZoneLocations().getLocation2();
            out.append("location1=" + l1.getWorld().getName() + ":" + l1.getBlockX() + ":" + l1.getBlockY() + ":" + l1.getBlockZ());
            
            out.newLine();
            
            out.append("location2=" + l2.getWorld().getName() + ":" + l2.getBlockX() + ":" + l2.getBlockY() + ":" + l2.getBlockZ());
            
            out.newLine();
            
            out.append("flags=");
            for(Entry<KothFlag, Integer> flag : koth.getFlags().entrySet()) {
                out.append(flag.getKey().toString() + ":" + flag.getValue() + "|");
            }
            
            if(koth.getLootTable() != null) {
                out.newLine();
                
                out.append("loottable=" + koth.getLootTable().getName());
            }
            
            out.newLine();
            
            out.append("capTimeMessages=");
            for(Entry<Long, String> entry : koth.getCapTimeMessages().entrySet())
                out.append(entry.getKey() + ":" + entry.getValue().replace(ChatColor.COLOR_CHAR, '&') + "|");
            out.newLine();
            
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " Exception thrown while writing to file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
    public static void loadAllKoths() {
        File[] files = dataDir.listFiles();
        
        NexGenKoths.loadedKoths.clear();
        
        for(File file : files) {
            loadKoth(file);
        }
    }
    
    
    public static void loadKoth(File file) {
        try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            String name = null;
            Location l1 = null;
            Location l2 = null;
            Map<KothFlag, Integer> flags = new HashMap<KothFlag, Integer>();
            Map<Long, String> capTimeMessages = new HashMap<Long, String>();
            String lootTableName = "";
            
            String line;
            while((line = in.readLine()) != null) {
                String[] split = line.split("\\=");
                
                if(split.length != 2) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " Length of line \"" + line + "\" when split by \"\\=\" isn't 2. Ignoring line.");
                    continue;
                }
                
                if(split[0].equalsIgnoreCase("name")) // Name
                    name = split[1];
                else if(split[0].equalsIgnoreCase("location1")) { // Location 1
                    String[] locDetails = split[1].split("\\:");
                    
                    if(locDetails.length != 4) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + split[1] + "\" when split by \"\\:\" isn't 4. Ignoring line.");
                        continue;
                    }
                    
                    String worldName, x, y, z;
                    
                    worldName = locDetails[0];
                    x = locDetails[1];
                    y = locDetails[2];
                    z = locDetails[3];
                    
                    if(!NumberUtils.isInteger(x) || !NumberUtils.isInteger(y) || !NumberUtils.isInteger(z)) {
                        Bukkit.getLogger().warning(String.format(NexGenKoths.tag + " X, Y and Z \"%s, %s, %s\" are not valid integers. Ignoring line.", x, y, z));
                        continue;
                    }
                    
                    World world = Bukkit.getWorld(worldName);
                    int xInt = Integer.parseInt(x), yInt = Integer.parseInt(y), zInt = Integer.parseInt(z);
                    
                    if(world == null) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " World \"" + worldName + "\" was not found. Ignoring line.");
                        continue;
                    }
                    
                    l1 = new Location(world, xInt, yInt, zInt);
                }
                else if(split[0].equalsIgnoreCase("location2")) { // Location 2
                    String[] locDetails = split[1].split("\\:");
                    
                    if(locDetails.length != 4) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + split[1] + "\" when split by \"\\:\" isn't 4. Ignoring line.");
                        continue;
                    }
                    
                    String worldName, x, y, z;
                    
                    worldName = locDetails[0];
                    x = locDetails[1];
                    y = locDetails[2];
                    z = locDetails[3];
                    
                    if(!NumberUtils.isInteger(x) || !NumberUtils.isInteger(y) || !NumberUtils.isInteger(z)) {
                        Bukkit.getLogger().warning(String.format(NexGenKoths.tag + " X, Y and Z \"%s, %s, %s\" are not valid integers. Ignoring line.", x, y, z));
                        continue;
                    }
                    
                    World world = Bukkit.getWorld(worldName);
                    int xInt = Integer.parseInt(x), yInt = Integer.parseInt(y), zInt = Integer.parseInt(z);
                    
                    if(world == null) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " World \"" + worldName + "\" was not found. Ignoring line.");
                        continue;
                    }
                    
                    l2 = new Location(world, xInt, yInt, zInt);
                }
                else if(split[0].equalsIgnoreCase("flags")) { // Flags
                    String[] flagsSplit = split[1].split("\\|");
                    
                    for(String str : flagsSplit) {
                        String[] flagValueSplit = str.split("\\:");
                        
                        if(flagValueSplit.length != 2) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + str + "\" when split by \"\\:\" isn't 2. Ignoring line.");
                            continue;
                        }
                        
                        KothFlag flag;
                        int value;
                        
                        try {
                            flag = KothFlag.valueOf(flagValueSplit[0]);
                        } catch(IllegalArgumentException ex) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + flagValueSplit[0] + "\" isn't a valid KoTH flag. Ignoring line.");
                            continue;
                        }
                        
                        if(!NumberUtils.isInteger(flagValueSplit[1])) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " \"" + flagValueSplit[1] + "\" isn't a valid integer. Ignoring line.");
                            continue;
                        }
                        
                        value = Integer.parseInt(flagValueSplit[1]);
                        
                        flags.put(flag, value);
                    }
                }
                else if(split[0].equalsIgnoreCase("loottable")) { // LootTable
                    lootTableName = split[1];
                }
                else if(split[0].equalsIgnoreCase("capTimeMessages")) { // capTimeMessages
                    String[] ctmSplit = split[1].split("\\|");
                    
                    for(String str : ctmSplit) {
                        String[] sectSplit = str.split("\\:");
                        
                        if(sectSplit.length != 2) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + str + "\" when split by \"\\:\" isn't 2. Ignoring line.");
                            continue;
                        }
                        
                        if(!NumberUtils.isLong(sectSplit[0])) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " \"" + sectSplit[0] + "\" isn't a valid long. Ignoring line.");
                            continue;
                        }
                        
                        long time = Long.parseLong(sectSplit[0]);
                        String message = sectSplit[1];
                        
                        capTimeMessages.put(time, ChatColor.translateAlternateColorCodes('&', message));
                    }
                }
            }
            
            if(name == null || l1 == null || l2 == null) {
                Bukkit.getLogger().severe(NexGenKoths.tag + " Error while loading KoTH from file \"" + file.getAbsolutePath() + "\". Name, Location1 or Location 2 is null.");
                Bukkit.getLogger().severe(String.format(NexGenKoths.tag + " Name: %s, Location 1: %s, Location 2: %s", name != null, l1 != null, l2 != null));
                Bukkit.getLogger().severe(NexGenKoths.tag + " Aborting the load of KoTH from file \"" + file.getAbsolutePath() + "\"");
                return;
            }
            
            LootTable lootTable = NexGenKoths.getLootTableByName(lootTableName);
            
            Koth koth = new Koth(name, new LocationPair(l1, l2), flags, capTimeMessages);
            if(lootTable != null)
                koth.setLootTable(lootTable);
            
            NexGenKoths.loadedKoths.add(koth);
            
            koth.startAutoStartTimer();
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " Exception thrown while reading from file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
    public static void deleteKoth(Koth koth) {
        if(koth == null) {
            Bukkit.getLogger().severe(NexGenKoths.tag + " Tried to delete a null KoTH.");
            Bukkit.getLogger().severe(NexGenKoths.tag + " Aborting KoTH deletion.");
            return;
        }
        
        File file = new File(dataDir, koth.getName());
        
        if(!file.delete()) {
            Bukkit.getLogger().severe(NexGenKoths.tag + " Error deleting KoTH \"" + koth.getName() + "\": Couldn't delete file \"" + file.getAbsolutePath() + "\"");
            Bukkit.getLogger().severe(NexGenKoths.tag + " Aborting KoTH deletion.");
            return;
        }
        
        koth.stopAutoEndTimer();
        koth.stopAutoStartTimer();
        
        if(koth.isBeingCaptured())
            koth.stopCaptureTimer(koth.getCappingPlayer());
        
        if(NexGenKoths.loadedKoths.contains(koth))
            NexGenKoths.loadedKoths.remove(koth);
    }
    
    
}
