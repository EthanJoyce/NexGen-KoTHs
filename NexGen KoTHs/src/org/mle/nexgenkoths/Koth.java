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
        this(name, capZoneLocations, new HashMap<KothFlag, Integer>());
    }
    
    
    public void startAutoStartTimer() {
        long autoStart = getFlagValue(KothFlag.AUTO_START);
        
        if(autoStart == 0)
            return;
        
        final long AUTO_START_DELAY = getFlagValue(KothFlag.AUTO_START_DELAY);
        lastAutoStartDelay = AUTO_START_DELAY;
        
        if(autoStartTimerID != -1) {
            Bukkit.getLogger().severe(NexGenKoths.tag + " Error starting AutoStartTimer for KoTH \"" + name + "\": An AutoStartTimer task already exists.");
            return;
        }
        
        autoStartTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(NexGenKoths.instance, new Runnable() {
            public void run() {
                autoStartTimer++;
                
                if(autoStartTimer >= AUTO_START_DELAY) {
                    long minPlayersToStart = getFlagValue(KothFlag.MIN_PLAYERS_TO_START);
                    
                    stopAutoStartTimer();
                    
                    if(Bukkit.getOnlinePlayers().size() >= minPlayersToStart)
                        startKoth();
                    else
                        startAutoStartTimer();
                }
            }
        }, 20, 20);
    }
    
    public void stopAutoStartTimer() {
        if(autoStartTimerID != -1) {
            Bukkit.getScheduler().cancelTask(autoStartTimerID);
            autoStartTimerID = -1;
            autoStartTimer = 0;
            lastAutoStartDelay = -1;
        }
    }
    
    
    public void startAutoEndTimer() {
        long autoEnd = getFlagValue(KothFlag.AUTO_END);
        
        if(autoEnd == 0)
            return;
        
        final long AUTO_END_DELAY = getFlagValue(KothFlag.AUTO_END_DELAY);
        lastAutoEndDelay = AUTO_END_DELAY;
        
        if(autoEndTimerID != -1) {
            Bukkit.getLogger().severe(NexGenKoths.tag + " Error starting AutoEndTimer for KoTH \"" + name + "\": An AutoEndTimer task already exists.");
            return;
        }
        
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
    
    public void stopAutoEndTimer() {
        if(autoEndTimerID != -1) {
            Bukkit.getScheduler().cancelTask(autoEndTimerID);
            autoEndTimerID = -1;
            autoEndTimer = 0;
            lastAutoEndDelay = -1;
            
            if(NexGenKoths.useScoreboard && NexGenKoths.globalScoreboardsMap.containsKey(ChatColor.GREEN + name + " End"))
                NexGenKoths.globalScoreboardsMap.remove(ChatColor.GREEN + name + " End");
        }
    }
    
    
    public void startCaptureTimer(final Player player) {
        final long CAPTURE_TIME = getFlagValue(KothFlag.CAPTURE_TIME);
        
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
        stopAutoStartTimer();
        
        setActive(true);
        
        Bukkit.broadcastMessage(NexGenKoths.kothStartMsg.replace("{KOTH_NAME}", getName()));
        
        startAutoEndTimer();
    }
    
    
    public void stopKoth(boolean broadcast) {
        stopAutoEndTimer();
        
        setActive(false);
        
        if(isBeingCapped)
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
    
    
    public long getFlagValue(KothFlag flag) {
        long value = flag.getDefaultValue();
        
        if(flags.containsKey(flag))
            value = flags.get(flag);
        
        return value;
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
        long autoStartDelay = getFlagValue(KothFlag.AUTO_START_DELAY);
        
        if(lastAutoStartDelay != -1)
            autoStartDelay = lastAutoStartDelay;
        
        return autoStartDelay - autoStartTimer;
    }
    
    public long getAutoEndTimer() {
        long autoEndDelay = getFlagValue(KothFlag.AUTO_END_DELAY);
        
        if(lastAutoEndDelay != -1)
            autoEndDelay = lastAutoEndDelay;
        
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
