package org.mle.nexgenkoths;

public enum KothFlag {
    AUTO_START_DELAY(3600), AUTO_START(0), MIN_PLAYERS_TO_START(0), AUTO_END_DELAY(3600), AUTO_END(1), CAPTURE_TIME(90), USE_LOOT_TABLE(0), BROADCAST_CAPTURE(1);
    
    private long value;
    
    KothFlag(long value) {
        this.value = value;
    }
    
    
    public long getDefaultValue() {
        return value;
    }
    
    @Deprecated
    public void setDefaultValue(long value) {
        this.value = value;
    }
}
