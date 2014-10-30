package org.mle.nexgenkoths.customitems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mle.nexgenkoths.NexGenKoths;
import org.mle.nexgenkoths.util.NumberUtils;

public enum CustomItemsDataHandler {;
    
    private static File dataDir = new File(NexGenKoths.instance.getDataFolder(), "CustomItems");
    
    
    public static void initDirectories() {
        dataDir.mkdirs();
    }
    
    
    public static void loadAllCustomItems() {
        NexGenKoths.loadedCustomItems.clear();
        
        for(File file : dataDir.listFiles())
            loadCustomItem(file);
    }
    
    
    public static void loadCustomItem(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = config.getKeys(false);
        
        for(String customItemName : keys) {
            ConfigurationSection sect = config.getConfigurationSection(customItemName);
            
            Material material = Material.getMaterial(sect.getString("Item").toUpperCase());
            String displayName = sect.getString("Display");
            List<String> lore = new ArrayList<String>();
            short dura = (short) sect.getInt("Durability", 0);
            
            Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
            
            
            if(displayName != null)
                displayName = ChatColor.translateAlternateColorCodes('&', sect.getString("Display"));
            
            for(String loreStr : sect.getStringList("Lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', loreStr));
            }
            
            for(String enchantStr : sect.getStringList("Enchantments")) {
                String[] enchSplit = enchantStr.split("\\:");
                
                if(enchSplit.length != 2) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " Length of string \"" + enchantStr + "\" when split by \"\\:\" isn't 2.  Ignoring enchantments entry for Custom Item \"" + customItemName + "\".");
                    continue;
                }
                
                Enchantment ench = Enchantment.getByName(enchSplit[0]);
                String levelStr = enchSplit[1];
                
                if(ench == null) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " Enchantment \"" + enchSplit[0] + "\" doesn't exist. Ignoring enchantments entry for Custom Item \"" + customItemName + "\".");
                    continue;
                }
                
                if(!NumberUtils.isInteger(levelStr)) {
                    Bukkit.getLogger().warning(NexGenKoths.tag + " String \"" + levelStr + " is not a valid Integer. Ignoring enchantments entry for Custom Item \"" + customItemName + "\".");
                    continue;
                }
                
                
                enchantments.put(ench, Integer.parseInt(levelStr));
            }
            
            if(material == null) {
                Bukkit.getLogger().warning(NexGenKoths.tag + " Item Name \"" + sect.getString("Item") + " is not valid (Loading Custom Item: \"" + customItemName + "\"). Ignoring item.");
                continue;
            }
            
            ItemStack itemStack = new ItemStack(material);
            itemStack.setDurability(dura);
            
            ItemMeta meta = itemStack.getItemMeta();
              if(displayName != null) meta.setDisplayName(displayName);
              if(lore != null) meta.setLore(lore);
            itemStack.setItemMeta(meta);
            
            itemStack.addUnsafeEnchantments(enchantments);
            
            
            NexGenKoths.loadedCustomItems.add(new CustomItem(customItemName, itemStack));
        }
    }
    
    
    public static void createExampleCustomItems() {
        File file = new File(dataDir, "ExampleCustomItems");
        
        List<String> lines = new ArrayList<String>(Arrays.asList(new String[] {
            "BowOfExamples:",
            "  Item: bow",
            "  Display: '&6Bow of Examples'",
            "  Lore:",
            "  - '&dThis is the first line of lore!'",
            "  - '&5And this is the second...'",
            "  Durability: 26",
            "  Enchantments:",
            "  - DURABILITY:5",
            "  - ARROW_DAMAGE:9",
            "  - ARROW_INFINITE:2",
            "PureGold:",
            "  Item: gold_ingot",
            "  Display: '&e&l&nPure Gold'",
            "TypicalDiamond:",
            "  Item: diamond"
        }));
        
        try(BufferedWriter out = new BufferedWriter(new PrintWriter(file))) {
            for(String str : lines) {
                out.append(str);
                out.newLine();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getLogger().severe(NexGenKoths.tag + " Exception thrown while writing to file \"" + file.getAbsolutePath() + "\": " + ex.getMessage());
        }
    }
    
    
}
