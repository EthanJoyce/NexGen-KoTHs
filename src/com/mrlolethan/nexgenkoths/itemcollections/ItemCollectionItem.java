package com.mrlolethan.nexgenkoths.itemcollections;

import org.bukkit.inventory.ItemStack;

public class ItemCollectionItem {
    
    private ItemStack itemStack;
    private AmountRange amtRange;
    
    
    public ItemCollectionItem(ItemStack itemStack, AmountRange amtRange) {
        this.itemStack = itemStack;
        this.amtRange = amtRange;
    }
    
    
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    public AmountRange getAmountRange() {
        return amtRange;
    }
    
    
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    public void setAmountRange(AmountRange amtRange) {
        this.amtRange = amtRange;
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
