package org.mle.nexgenkoths.loottables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.loottables.LootTableItem.AmountRange;
import org.mle.nexgenkoths.util.NumberUtils;

public class LootTableDataHandler {
    
    private static File dataDir = new File(NexGenKoths.instance.getDataFolder(), "LootTables");
    
    
    public static void initDirectories() {
        dataDir.mkdirs();
    }
    
    
    public static void loadAllLootTables() {
        NexGenKoths.loadedLootTables.clear();
        
        for(File file : dataDir.listFiles()) {
            loadLootTable(file);
        }
    }
    
    
    public static void loadLootTable(File file) {
        try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            List<LootTableItem> items = new ArrayList<LootTableItem>();
            
            String line;
            while((line = in.readLine()) != null) {
                String[] split = line.split("\\s");
                
                if(split.length != 3) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + line + "\" when split by \"\\s\" isn't 3. Ignoring line.");
                    continue;
                }
                
                String itemName = split[0].toUpperCase();
                String amtRangeStr = split[1];
                String chanceStr = split[2];
                
                Material material;
                AmountRange amtRange;
                float chance;
                
                short dura;
                
                if(itemName.contains(":")) {
                    String[] itemIdSplit = itemName.split("\\:");
                    
                    if(itemIdSplit.length != 2) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + itemName + "\" when split by \"\\:\" isn't 2. Ignoring line.");
                        continue;
                    }
                    
                    material = Material.getMaterial(itemIdSplit[0]);
                    
                    if(material == null) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Item ID \"" + itemName + " is not valid. Ignoring line.");
                        continue;
                    }
                    
                    if(!NumberUtils.isShort(itemIdSplit[1])) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + itemIdSplit[1] + " is not a valid short. Ignoring line.");
                        continue;
                    }
                    
                    dura = Short.parseShort(itemIdSplit[1]);
                } else {
                    material = Material.getMaterial(itemName);
                    dura = 0;
                    
                    if(material == null) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Item ID \"" + itemName + "\" is not valid. Ignoring line.");
                        continue;
                    }
                }
                
                if(amtRangeStr.contains("-")) {
                    String[] amtRangeSplit = amtRangeStr.split("\\-");
                    if(amtRangeSplit.length != 2) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + amtRangeStr + "\" when split by \"\\-\" isn't 2. Ignoring line.");
                        continue;
                    }
                    
                    if(!NumberUtils.isInteger(amtRangeSplit[0]) || !NumberUtils.isInteger(amtRangeSplit[1])) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + amtRangeSplit[0] + "\" isn't a valid integer. Ignoring line.");
                        continue;
                    }
                    
                    amtRange = new AmountRange(Integer.parseInt(amtRangeSplit[0]), Integer.parseInt(amtRangeSplit[1]));
                } else {
                    if(!NumberUtils.isInteger(amtRangeStr)) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + amtRangeStr + "\" isn't a valid integer. Ignoring line.");
                        continue;
                    }
                    
                    amtRange = new AmountRange(Integer.parseInt(amtRangeStr), Integer.parseInt(amtRangeStr));
                }
                
                if(!NumberUtils.isFloat(chanceStr)) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + chanceStr + "\" isn't a valid float. Ignoring line.");
                    continue;
                }
                
                chance = Float.parseFloat(chanceStr);
                
                
                LootTableItem item = new LootTableItem(material, amtRange, chance);
                item.setDurability(dura);
                items.add(item);
            }
            
            LootTable lootTable = new LootTable(file.getName(), items);
            NexGenKoths.loadedLootTables.add(lootTable);
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " Exception thrown while reading from file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
    public static void createExampleTable() {
        File file = new File(dataDir, "ExampleLootTable");
        
        try(BufferedWriter out = new BufferedWriter(new PrintWriter(file))) {
            out.append("diamond 1-32 1.0");
            
            out.newLine();
            
            out.append("emerald 1-2 0.3");
            
            out.newLine();
            
            out.append("golden_apple:1 1 0.1");
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " Exception thrown while writing to file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
}
