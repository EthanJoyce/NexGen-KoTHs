package org.mle.nexgenkoths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.loottables.LootTable;

public class Koth {
    
	private String name;
    private LocationPair capZoneLocations;
    private Map<KothFlag, Integer> flags;
    
    private LootTable lootTable;
    
    private boolean active = false;
    
    private long lastAutoStartDelay = -1;
    private long autoStartTimer = 0;
    private long autoEndTimer = 0;
    private long capTimer = 0;
    
    private long lastAutoEndDelay = -1;
    private int autoStartTimerID = -1;
    private int autoEndTimerID = -1;
    private int capTimerID = -1;
    
    private boolean isBeingCapped = false;
    private Player cappingPlayer;
    
    
    public Koth(String name, LocationPair capZoneLocations, Map<KothFlag, Integer> flags) {
        this.name = name;
        this.capZoneLocations = capZoneLocations;
        this.flags = flags;
    }
    
    public Koth(String name, LocationPair capZoneLocations) {
        this.name = name;
        this.capZoneLocations = capZoneLocations;
        this.flags = new HashMap<KothFlag, Integer>();
    }
    
    
    public void startAutoStartTimer() {
        long autoStart = KothFlag.AUTO_START.getDefaultValue();
        if(flags.containsKey(KothFlag.AUTO_START))
            autoStart = flags.get(KothFlag.AUTO_START);
        
        if(autoStart == 0)
            return;
        
        long autoStartDelay = KothFlag.AUTO_START_DELAY.getDefaultValue();
        if(flags.containsKey(KothFlag.AUTO_START_DELAY))
            autoStartDelay = flags.get(KothFlag.AUTO_START_DELAY);
        
        final long AUTO_START_DELAY = autoStartDelay;
        lastAutoStartDelay = autoStartDelay;
        
        autoStartTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(NexGenKoths.instance, new Runnable() {
            public void run() {
                autoStartTimer++;
                
                if(autoStartTimer >= AUTO_START_DELAY) {
                    Bukkit.getScheduler().cancelTask(autoStartTimerID);
                    autoStartTimerID = -1;
                    autoStartTimer = 0;
                    lastAutoStartDelay = -1;
                    
                    long minPlayersToStart = KothFlag.MIN_PLAYERS_TO_START.getDefaultValue();
                    if(flags.containsKey(KothFlag.MIN_PLAYERS_TO_START))
                        minPlayersToStart = flags.get(KothFlag.MIN_PLAYERS_TO_START);
                    
                    if(Bukkit.getOnlinePlayers().size() >= minPlayersToStart)
                        startKoth();
                    else
                        startAutoStartTimer();
                }
            }
        }, 20, 20);
    }
    
    
    public void startAutoEndTimer() {
        long autoEnd = KothFlag.AUTO_END.getDefaultValue();
        if(flags.containsKey(KothFlag.AUTO_END))
            autoEnd = flags.get(KothFlag.AUTO_END);
        
        if(autoEnd == 0)
            return;
        
        long autoEndDelay = KothFlag.AUTO_END_DELAY.getDefaultValue();
        if(flags.containsKey(KothFlag.AUTO_END_DELAY))
            autoEndDelay = flags.get(KothFlag.AUTO_END_DELAY);
        
        final long AUTO_END_DELAY = autoEndDelay;
        lastAutoEndDelay = autoEndDelay;
        
        autoEndTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(NexGenKoths.instance, new Runnable() {
            public void run() {
                autoEndTimer++;
                
                if(NexGenKoths.useScoreboard)
                    NexGenKoths.globalScoreboardsMap.put(ChatColor.GREEN + name + " End", (int) (AUTO_END_DELAY - autoEndTimer));
                
                if(autoEndTimer >= AUTO_END_DELAY) {
                    stopKoth(true);
                }
            }
        }, 20, 20);
    }
    
    
    public void startCaptureTimer(final Player player) {
        long capTime = KothFlag.CAPTURE_TIME.getDefaultValue();
        if(flags.containsKey(KothFlag.CAPTURE_TIME))
            capTime = flags.get(KothFlag.CAPTURE_TIME);
        
        final long CAPTURE_TIME = capTime;
        
        isBeingCapped = true;
        cappingPlayer = player;
        
        capTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(NexGenKoths.instance, new Runnable() {
            public void run() {
                capTimer++;
                
                if(NexGenKoths.useScoreboard)
                    NexGenKoths.globalScoreboardsMap.put(ChatColor.GREEN + name + " Cap", (int) (CAPTURE_TIME - capTimer));
                
                if(capTimer >= CAPTURE_TIME) {
                    playerCapturedKoth(player);
                    
                    stopCaptureTimer(player);
                }
            }
        }, 20, 20);
    }
    
    public void stopCaptureTimer(Player player) {
        Bukkit.getScheduler().cancelTask(capTimerID);
        capTimerID = -1;
        capTimer = 0;
        
        isBeingCapped = false;
        cappingPlayer = null;
        
        if(NexGenKoths.useScoreboard && NexGenKoths.globalScoreboardsMap.containsKey(ChatColor.GREEN + name + " Cap"))
            NexGenKoths.globalScoreboardsMap.remove(ChatColor.GREEN + name + " Cap");
        
        if(player == null)
            return;
    }
    
    
    public void playerCapturedKoth(Player player) {
        if(NexGenKoths.onPlayerCaptureKoth(player, this))
            stopKoth(false);
    }
    
    
    public void startKoth() {
        if(active) {
            if(autoEndTimerID == -1)
                startAutoEndTimer();
            
            return;
        }
        
        setActive(true);
        
        Bukkit.broadcastMessage(NexGenKoths.kothStartMsg.replace("{KOTH_NAME}", getName()));
        
        startAutoEndTimer();
    }
    
    
    public void stopKoth(boolean broadcast) {
        if(!active) {
            if(autoStartTimerID == -1)
                startAutoStartTimer();
            
            return;
        }
        
        if(autoEndTimerID != -1) {
            Bukkit.getScheduler().cancelTask(autoEndTimerID);
            autoEndTimerID = -1;
            autoEndTimer = 0;
            lastAutoEndDelay = -1;
            
            if(NexGenKoths.useScoreboard)
                NexGenKoths.globalScoreboardsMap.put(ChatColor.GREEN + name + " End", 0);
        }
        
        setActive(false);
        
        stopCaptureTimer(null);
        
        if(broadcast)
            Bukkit.broadcastMessage(NexGenKoths.kothStopMsg.replace("{KOTH_NAME}", getName()));
        
        startAutoStartTimer();
    }
    
    
    public List<ItemStack> getRandomLoot() {
        if(lootTable != null)
            return lootTable.getRandomLoot();
        else
            return null;
    }
    
    
    public Map<String, Double> getRandomNonItemLoot() {
        if(lootTable != null)
            return lootTable.getRandomNonItemLoot();
        else
            return null;
    }
    
    
    public String getName() {
        return name;
    }
    
    public LocationPair getCapZoneLocations() {
        return capZoneLocations;
    }
    
    public Map<KothFlag, Integer> getFlags() {
        return flags;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean isBeingCaptured() {
        return isBeingCapped;
    }
    
    public LootTable getLootTable() {
        return lootTable;
    }
    
    public Player getCappingPlayer() {
        return cappingPlayer;
    }
    
    public long getAutoStartTimer() {
        long autoStartDelay = KothFlag.AUTO_START_DELAY.getDefaultValue();
        
        if(lastAutoStartDelay != -1) {
            autoStartDelay = lastAutoStartDelay;
        } else {
            if(flags.containsKey(KothFlag.AUTO_START_DELAY))
                autoStartDelay = flags.get(KothFlag.AUTO_START_DELAY);
        }
        
        return autoStartDelay - autoStartTimer;
    }
    
    public long getAutoEndTimer() {
        long autoEndDelay = KothFlag.AUTO_END_DELAY.getDefaultValue();
        
        if(lastAutoEndDelay != -1) {
            autoEndDelay = lastAutoEndDelay;
        } else {
            if(flags.containsKey(KothFlag.AUTO_END_DELAY))
                autoEndDelay = flags.get(KothFlag.AUTO_END_DELAY);
        }
        
        return autoEndDelay - autoEndTimer;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCapZoneLocations(LocationPair capZoneLocations) {
        this.capZoneLocations = capZoneLocations;
    }
    
    @Deprecated
    public void setFlags(Map<KothFlag, Integer> flags) {
        this.flags = flags;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Deprecated
    public void setBeingCaptured(boolean isBeingCaptured) {
        isBeingCapped = isBeingCaptured;
    }
    
    @Deprecated
    public void setCappingPlayer(Player cappingPlayer) {
        this.cappingPlayer = cappingPlayer;
    }
    
    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }
    
    
}
