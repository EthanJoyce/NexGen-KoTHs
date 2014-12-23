package com.mrlolethan.nexgenkoths.itemcollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import com.mrlolethan.nexgenkoths.itemcollections.ItemCollectionItem.AmountRange;

public class ItemCollection {
    
    private String name;
    private List<ItemCollectionItem> items;
    
    private Random random = new Random();
    
    
    public ItemCollection(String name, List<ItemCollectionItem> items) {
        this.name = name;
        this.items = items;
    }
    
    public ItemCollection(String name) {
        this(name, new ArrayList<ItemCollectionItem>());
    }
    
    
    public String getName() {
        return name;
    }
    
    public List<ItemCollectionItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public ItemStack getRandomItem() {
        ItemCollectionItem item = items.get(random.nextInt(items.size()));
        ItemStack itemStack = item.getItemStack();
        
        AmountRange range = item.getAmountRange();
        itemStack.setAmount(random.nextInt((range.getMax() - range.getMin()) + 1) + range.getMin());
        
        return itemStack;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void removeItem(ItemStack itemStack) {
        items.remove(itemStack);
    }
    
    
}
