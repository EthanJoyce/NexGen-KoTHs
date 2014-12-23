package com.mrlolethan.nexgenkoths.itemcollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.P;
import com.mrlolethan.nexgenkoths.customitems.CustomItem;
import com.mrlolethan.nexgenkoths.itemcollections.ItemCollectionItem.AmountRange;
import com.mrlolethan.nexgenkoths.util.NumberUtils;

public enum ItemCollectionDataHandler {;
    
    private static File dataDir = new File(P.p.getDataFolder(), "ItemCollections");
    
    
    public static void initDirectories() {
        dataDir.mkdirs();
    }
    
    
    public static void loadAllItemCollections() {
        NexGenKoths.loadedItemCollections.clear();
        
        for(File file : dataDir.listFiles())
            loadItemCollection(file);
    }
    
    
    public static void loadItemCollection(File file) {
        try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            List<ItemCollectionItem> items = new ArrayList<ItemCollectionItem>();
            
            String line;
            while((line = in.readLine()) != null) {
                String[] split = line.split("\\s");
                
                if(split.length != 2) {
                    P.log(Level.WARNING, "Length of string \"" + line + "\" when split by \"\\s\" isn't 2. Ignoring line.");
                    continue;
                }
                
                String itemName = split[0].toUpperCase();
                String amtRangeStr = split[1];
                
                Material material;
                AmountRange amtRange = null;
                
                short dura;
                
                CustomItem customItem = NexGenKoths.getCustomItemByName(split[0]);
                boolean isCustomItem = false;
                
                if(itemName.contains(":")) {
                    String[] itemNameSplit = itemName.split("\\:");
                    
                    if(itemNameSplit.length != 2) {
                        P.log(Level.WARNING, "Length of string \"" + itemName + "\" when split by \"\\:\" isn't 2. Ignoring line.");
                        continue;
                    }
                    
                    material = Material.getMaterial(itemNameSplit[0]);
                    
                    if(material == null) {
                        if(customItem == null) {
                            P.log(Level.WARNING, "Item Name \"" + split[0] + " is not valid. Ignoring line.");
                            continue;
                        } else {
                            isCustomItem = true;
                        }
                    }
                    
                    if(!NumberUtils.isShort(itemNameSplit[1])) {
                        P.log(Level.WARNING, "String \"" + itemNameSplit[1] + " is not a valid short. Ignoring line.");
                        continue;
                    }
                    
                    dura = Short.parseShort(itemNameSplit[1]);
                } else {
                    material = Material.getMaterial(itemName);
                    dura = 0;
                    
                    if(material == null) {
                        if(customItem == null) {
                            P.log(Level.WARNING, "Item Name \"" + split[0] + " is not valid. Ignoring line.");
                            continue;
                        } else {
                            isCustomItem = true;
                        }
                    }
                }
                
                if(amtRangeStr.contains("-")) {
                    String[] amtRangeSplit = amtRangeStr.split("\\-");
                    
                    if(amtRangeSplit.length != 2) {
                        P.log(Level.WARNING, "Length of string \"" + amtRangeStr + "\" when split by \"\\-\" isn't 2. Ignoring line.");
                        continue;
                    }
                    
                    if(!NumberUtils.isInteger(amtRangeSplit[0]) || !NumberUtils.isInteger(amtRangeSplit[1])) {
                        P.log(Level.WARNING, "String \"" + amtRangeSplit[0] + "\" isn't a valid integer. Ignoring line.");
                        continue;
                    }
                    
                    amtRange = new AmountRange(Integer.parseInt(amtRangeSplit[0]), Integer.parseInt(amtRangeSplit[1]));
                } else {
                    if(!NumberUtils.isInteger(amtRangeStr)) {
                        P.log(Level.WARNING, "String \"" + amtRangeStr + "\" isn't a valid integer. Ignoring line.");
                        continue;
                    }
                    
                    amtRange = new AmountRange(Integer.parseInt(amtRangeStr), Integer.parseInt(amtRangeStr));
                }
                
                
                if(!isCustomItem) { // Item is NOT a custom item
                    ItemCollectionItem item = new ItemCollectionItem(new ItemStack(material), amtRange);
                    item.getItemStack().setDurability(dura);
                    items.add(item);
                } else { // Item is a custom item
                    ItemCollectionItem item = new ItemCollectionItem(customItem.getItemStack(), amtRange);
                    items.add(item);
                }
            }
            
            ItemCollection itemCollection = new ItemCollection(file.getName(), items);
            NexGenKoths.loadedItemCollections.add(itemCollection);
        } catch(Exception ex) {
            ex.printStackTrace();
            P.log(Level.SEVERE, "Exception thrown while reading from file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
    public static void createExampleItemCollection() {
        File file = new File(dataDir, "ExampleItemCollection");
        
        try(BufferedWriter out = new BufferedWriter(new PrintWriter(file))) {
            out.append("diamond 1-7");
            
            out.newLine();
            
            out.append("emerald 4");
            
            out.newLine();
            
            out.append("golden_apple:1 3-10");
            
            out.newLine();
            
            out.append("BowOfExamples 1");
            
            out.newLine();
            
            out.append("PureGold 1-64");
        } catch(Exception ex) {
            ex.printStackTrace();
            P.log(Level.SEVERE, "Exception thrown while writing to file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
}
