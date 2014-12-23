package com.mrlolethan.nexgenkoths.mleutils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public enum InventoryUtils {;
    
    public static boolean givePlayerItemStack(Player player, ItemStack itemStack) {
        if(hasSpace(player, itemStack)) {
            player.getInventory().addItem(itemStack);
            return true;
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            return false;
        }
    }
    
    
    public static boolean hasSpace(Player player, ItemStack itemStack) {
        Inventory inv = player.getInventory();
        
        int slotsNeeded = 1;
        if(itemStack.getAmount() > itemStack.getMaxStackSize())
            slotsNeeded = (int) Math.ceil((double) itemStack.getAmount() / itemStack.getType().getMaxStackSize());
        
        int slotsAvailable = inv.getSize();
        for(ItemStack item : inv.getContents())
            if(item != null && !item.getType().equals(Material.AIR)) slotsAvailable--;
        
        if(slotsNeeded <= slotsAvailable)
            return true;
        
        return false;
    }
    
    
    public static int getItemAmountInInventory(Inventory inv, ItemStack itemStack, boolean checkMeta) {
        int amt = 0;
        
        for(ItemStack item : inv.getContents()) {
            if(item == null) continue;
            
            if(checkMeta) {
                if(item.getType().equals(itemStack.getType()) && item.getItemMeta().equals(itemStack.getItemMeta()))
                    amt += item.getAmount();
            } else {
                if(item.getType().equals(itemStack.getType()))
                    amt += item.getAmount();
            }
        }
        
        return amt;
    }
    
    
}
