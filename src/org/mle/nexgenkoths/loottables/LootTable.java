package org.mle.nexgenkoths.loottables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.itemcollections.ItemCollection;
import org.mle.nexgenkoths.loottables.LootTableItem.AmountRange;

public class LootTable {
    
    private String name = "";
    private List<LootTableItem> items;
    private List<NonItemLoot> nonItemLootList;
    private List<ItemCollection> itemCollections;
    
    private Random random = new Random();
    
    
    public LootTable(String name, List<LootTableItem> items, List<NonItemLoot> nonItemLootList, List<ItemCollection> itemCollections) {
        this.name = name;
        this.items = items;
        this.nonItemLootList = nonItemLootList;
        this.itemCollections = itemCollections;
    }
    
    public LootTable(String name, List<LootTableItem> items, List<NonItemLoot> nonItemLootList) {
        this(name, items, nonItemLootList, new ArrayList<ItemCollection>());
    }
    
    public LootTable(String name, List<LootTableItem> items) {
        this(name, items, new ArrayList<NonItemLoot>(), new ArrayList<ItemCollection>());
    }
    
    
    public List<ItemStack> getRandomLoot() {
        List<ItemStack> itemsList = new ArrayList<ItemStack>();
        
        for(LootTableItem item : items) {
            if(!(random.nextFloat() <= item.getChance())) continue;
            
            ItemStack itemStack = new ItemStack(item.getItemStack());
            itemStack.setDurability(item.getDurability());
            
            AmountRange range = item.getAmountRange();
            itemStack.setAmount(random.nextInt((range.getMax() - range.getMin()) + 1) + range.getMin());
            
            itemsList.add(itemStack);
        }
        
        for(ItemCollection coll : itemCollections) {
            itemsList.add(coll.getRandomItem());
        }
        
        return itemsList;
    }
    
    
    public Map<String, Double> getRandomNonItemLoot() {
        Map<String, Double> nonItemLoot = new HashMap<String, Double>();
        
        for(NonItemLoot loot : nonItemLootList) {
            if(!(random.nextFloat() <= loot.getChance())) continue;
            
            NonItemLoot.AmountRange range = loot.getAmountRange();
            
            nonItemLoot.put(loot.getName(), range.getMin() + (range.getMax() - range.getMin()) * random.nextDouble());
        }
        
        return nonItemLoot;
    }
    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public List<NonItemLoot> getNonItemLootList() {
        return nonItemLootList;
    }
    
    public void setNonItemLootList(List<NonItemLoot> nonItemLootList) {
        this.nonItemLootList = nonItemLootList;
    }
    
    
    public List<LootTableItem> getItems() {
        return items;
    }
    
    public void setItems(List<LootTableItem> items) {
        this.items = items;
    }
    
    
    public List<ItemCollection> getItemCollections() {
        return itemCollections;
    }
    
    public void setItemCollections(List<ItemCollection> itemCollections) {
        this.itemCollections = itemCollections;
    }
    
    
}
