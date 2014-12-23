package com.mrlolethan.nexgenkoths.loottables;

import org.bukkit.inventory.ItemStack;

public class LootTableItem {
    
    private ItemStack itemStack;
    private AmountRange amtRange;
    private float chance;
    
    private short durability;
    
    
    public LootTableItem(ItemStack itemStack, AmountRange amtRange, float chance) {
        this.itemStack = itemStack;
        this.amtRange = amtRange;
        this.chance = chance;
        
        this.durability = itemStack.getDurability();
    }
    
    
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    public AmountRange getAmountRange() {
        return amtRange;
    }
    
    public float getChance() {
        return chance;
    }
    
    public short getDurability() {
        return durability;
    }
    
    
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    public void setAmountRange(AmountRange amtRange) {
        this.amtRange = amtRange;
    }
    
    public void setChance(float chance) {
        this.chance = chance;
    }
    
    public void setDurability(short durability) {
        this.durability = durability;
    }
    
    
    public static class AmountRange {
        private int min, max;
        
        public AmountRange(int min, int max) {
            this.min = min;
            this.max = max;
        }
        
        public int getMax() {
            return max;
        }
        
        public int getMin() {
            return min;
        }
    }
    
}
