package org.mle.nexgenkoths.loottables;


public class NonItemLoot {
    
    private String name;
    private AmountRange amtRange;
    private float chance;
    
    
    public NonItemLoot(String name, AmountRange amtRange, float chance) {
        this.name = name;
        this.amtRange = amtRange;
        this.chance = chance;
    }
    
    
    public String getName() {
        return name;
    }
    
    public AmountRange getAmountRange() {
        return amtRange;
    }
    
    public float getChance() {
        return chance;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAmountRange(AmountRange amtRange) {
        this.amtRange = amtRange;
    }
    
    public void setChance(float chance) {
        this.chance = chance;
    }
    
    
    public static class AmountRange {
        private double min, max;
        
        public AmountRange(double min, double max) {
            this.min = min;
            this.max = max;
        }
        
        public double getMax() {
            return max;
        }
        
        public double getMin() {
            return min;
        }
    }
    
    
}
