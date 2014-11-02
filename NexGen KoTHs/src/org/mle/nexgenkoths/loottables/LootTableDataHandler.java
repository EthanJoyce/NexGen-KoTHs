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
import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.customitems.CustomItem;
import org.mle.nexgenkoths.loottables.LootTableItem.AmountRange;
import org.mle.nexgenkoths.util.NumberUtils;

public enum LootTableDataHandler {;
    
    private static File dataDir = new File(NexGenKoths.instance.getDataFolder(), "LootTables");
    
    
    public static void initDirectories() {
        dataDir.mkdirs();
    }
    
    
    public static void loadAllLootTables() {
        NexGenKoths.loadedLootTables.clear();
        
        for(File file : dataDir.listFiles())
            loadLootTable(file);
    }
    
    
    public static void loadLootTable(File file) {
        try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            List<LootTableItem> items = new ArrayList<LootTableItem>();
            List<NonItemLoot> nonItemLootList = new ArrayList<NonItemLoot>();
            
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
                LootTableItem.AmountRange amtRange = null;
                NonItemLoot.AmountRange nonItemLootAmtRange = null;
                float chance;
                
                short dura;
                
                CustomItem customItem = NexGenKoths.getCustomItemByName(split[0]);
                boolean isCustomItem = false;
                
                boolean isNonItemLoot = false;
                
                if(itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split("\\:");
                    
                    if(itemNameSplit.length != 2) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + itemName + "\" when split by \"\\:\" isn't 2. Ignoring line.");
                        continue;
                    }
                    
                    material = Material.getMaterial(itemNameSplit[0]);
                    
                    if(material == null) {
                        if(customItem == null) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " Item Name \"" + split[0] + " is not valid. Ignoring line.");
                            continue;
                        } else {
                            isCustomItem = true;
                        }
                    }
                    
                    if(!NumberUtils.isShort(itemNameSplit[1])) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + itemNameSplit[1] + " is not a valid short. Ignoring line.");
                        continue;
                    }
                    
                    dura = Short.parseShort(itemNameSplit[1]);
                } else {
                    material = Material.getMaterial(itemName);
                    dura = 0;
                    
                    if(material == null) {
                        if(customItem == null) {
                            isNonItemLoot = true;
                        } else {
                            isCustomItem = true;
                        }
                    }
                }
                
                if(amtRangeStr.contains("-")) {
                    String[] amtRangeSplit = amtRangeStr.split("\\-");
                    
                    if(amtRangeSplit.length != 2) {
                        Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + amtRangeStr + "\" when split by \"\\-\" isn't 2. Ignoring line.");
                        continue;
                    }
                    
                    if(!isNonItemLoot) { // Isn't a non-item loot
                        if(!NumberUtils.isInteger(amtRangeSplit[0]) || !NumberUtils.isInteger(amtRangeSplit[1])) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + amtRangeSplit[0] + "\" isn't a valid integer. Ignoring line.");
                            continue;
                        }
                        
                        amtRange = new AmountRange(Integer.parseInt(amtRangeSplit[0]), Integer.parseInt(amtRangeSplit[1]));
                    } else { // Isn't a non-item loot
                        if(!NumberUtils.isDouble(amtRangeSplit[0]) || !NumberUtils.isDouble(amtRangeSplit[1])) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + amtRangeSplit[0] + "\" isn't a valid double. Ignoring line.");
                            continue;
                        }
                        
                        nonItemLootAmtRange = new NonItemLoot.AmountRange(Double.parseDouble(amtRangeSplit[0]), Double.parseDouble(amtRangeSplit[1]));
                    }
                } else {
                    
                    if(!isNonItemLoot) { // Isn't a non-item loot
                        if(!NumberUtils.isInteger(amtRangeStr)) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + amtRangeStr + "\" isn't a valid integer. Ignoring line.");
                            continue;
                        }
                        
                        amtRange = new AmountRange(Integer.parseInt(amtRangeStr), Integer.parseInt(amtRangeStr));
                    } else { // IS a non-item loot
                        if(!NumberUtils.isDouble(amtRangeStr)) {
                            Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + amtRangeStr + "\" isn't a valid double. Ignoring line.");
                            continue;
                        }
                        
                        nonItemLootAmtRange = new NonItemLoot.AmountRange(Double.parseDouble(amtRangeStr), Double.parseDouble(amtRangeStr));
                    }
                }
                
                if(isNonItemLoot && nonItemLootAmtRange == null) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " nonItemLootAmtRange is null. Ignoring line.");
                    continue;
                }
                else if(!isNonItemLoot && amtRange == null) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " amtRange is null. Ignoring line.");
                    continue;
                }
                
                
                if(!NumberUtils.isFloat(chanceStr)) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + chanceStr + "\" isn't a valid float. Ignoring line.");
                    continue;
                }
                
                chance = Float.parseFloat(chanceStr);
                
                
                if(!isCustomItem && !isNonItemLoot) { // Item is NOT a custom item or nonItemLoot
                    LootTableItem item = new LootTableItem(new ItemStack(material), amtRange, chance);
                    item.setDurability(dura);
                    items.add(item);
                }
                else if(isCustomItem) { // Item is a custom item
                    LootTableItem item = new LootTableItem(customItem.getItemStack(), amtRange, chance);
                    items.add(item);
                }
                else if(isNonItemLoot) { // Item is a non-item loot
                    nonItemLootList.add(new NonItemLoot(split[0], nonItemLootAmtRange, chance));
                }
            }
            
            LootTable lootTable = new LootTable(file.getName(), items, nonItemLootList);
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
            
            out.newLine();
            
            out.append("BowOfExamples 1 0.25");
            
            out.newLine();
            
            out.append("PureGold 4-32 1");
            
            out.newLine();
            
            out.append("factionspower 1.30-5.12 1");
            
            out.newLine();
            
            out.append("exp 100-200 1");
            
            out.newLine();
            
            out.append("money 10.46-1000.94 1");
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " Exception thrown while writing to file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
}
