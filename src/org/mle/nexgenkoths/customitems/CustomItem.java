package org.mle.nexgenkoths.customitems;

import org.bukkit.inventory.ItemStack;

public class CustomItem {
    
    private String name;
    private ItemStack itemStack;
    
    
    public CustomItem(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }
    
    
    public String getName() {
        return name;
    }
    
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    
}
