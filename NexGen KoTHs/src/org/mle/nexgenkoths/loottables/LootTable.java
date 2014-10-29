package org.mle.nexgenkoths.loottables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.loottables.LootTableItem.AmountRange;

public class LootTable {
    
    private String name = "";
    private List<LootTableItem> items;
    
    private Random random = new Random();
    
    
    public LootTable(String name, List<LootTableItem> items) {
        this.name = name;
        this.items = items;
    }
    
    
    public List<ItemStack> getRandomLoot() {
        List<ItemStack> itemsList = new ArrayList<ItemStack>();
        
        for(LootTableItem item : items) {
            if(!(random.nextFloat() <= item.getChance())) continue;
            
            ItemStack itemStack = new ItemStack(item.getType());
            itemStack.setDurability(item.getDurability());
            
            AmountRange range = item.getAmountRange();
            itemStack.setAmount(random.nextInt((range.getMax() - range.getMin()) + 1) + range.getMin());
            
            itemsList.add(itemStack);
        }
        
        return itemsList;
    }
    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public List<LootTableItem> getItems() {
        return items;
    }
    
    public void setItems(List<LootTableItem> items) {
        this.items = items;
    }
    
    
}
