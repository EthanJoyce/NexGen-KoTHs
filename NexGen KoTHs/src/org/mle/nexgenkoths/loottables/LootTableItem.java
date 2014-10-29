package org.mle.nexgenkoths.loottables;

import org.bukkit.Material;

public class LootTableItem {
    
    private Material type;
    private AmountRange amtRange;
    private float chance;
    
    private short durability;
    
    
    public LootTableItem(Material type, AmountRange amtRange, float chance) {
        this.type = type;
        this.amtRange = amtRange;
        this.chance = chance;
        
        this.durability = 0;
    }
    
    
    public Material getType() {
        return type;
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
    
    
    public void setType(Material type) {
        this.type = type;
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
