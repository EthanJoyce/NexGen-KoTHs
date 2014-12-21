package org.mle.nexgenkoths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mle.nexgenkoths.events.PlayerCaptureKothEvent;
import org.mle.nexgenkoths.integration.Factions;
import org.mle.nexgenkoths.integration.Vault;
import org.mle.nexgenkoths.loottables.LootTable;
import org.mle.nexgenkoths.mleutils.InventoryUtils;

public class Koth {
    
	private String name;
    private LocationPair capZoneLocations;
    private Map<KothFlag, Integer> flags;
    private Map<Long, String> capTimeMessages;
    
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
    
    
    public Koth(String name, LocationPair capZoneLocations, Map<KothFlag, Integer> flags, Map<Long, String> capTimeMessages) {
        this.name = name;
        this.capZoneLocations = capZoneLocations;
        this.flags = flags;
        this.capTimeMessages = capTimeMessages;
    }
    
    public Koth(String name, LocationPair capZoneLocations, Map<KothFlag, Integer> flags) {
        this(name, capZoneLocations, flags, new HashMap<Long, String>());
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
            P.log(Level.SEVERE, "Error starting AutoStartTimer for KoTH \"" + name + "\": An AutoStartTimer task already exists.");
            return;
        }
        
        autoStartTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable() {
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
            P.log(Level.SEVERE, " Error starting AutoEndTimer for KoTH \"" + name + "\": An AutoEndTimer task already exists.");
            return;
        }
        
        autoEndTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable() {
            public void run() {
                autoEndTimer++;
                
                if(NexGenKoths.useScoreboard)
                    NexGenKoths.globalScoreboardsMap.put(ChatColor.GREEN + name + " Ends", (int) (AUTO_END_DELAY - autoEndTimer));
                
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
            
            if(NexGenKoths.useScoreboard && NexGenKoths.globalScoreboardsMap.containsKey(ChatColor.GREEN + name + " Ends"))
                NexGenKoths.globalScoreboardsMap.remove(ChatColor.GREEN + name + " Ends");
        }
    }
    
    
    public void startCaptureTimer(Player player) {
    	startCaptureTimer(player, true);
    }
    
    public void startCaptureTimer(final Player player, boolean broadcast) {
        final long CAPTURE_TIME = getFlagValue(KothFlag.CAPTURE_TIME);
        
        isBeingCapped = true;
        cappingPlayer = player;
        
        capTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(P.p, new Runnable() {
            public void run() {
                capTimer++;
                
                String capTimeMessage = getCapTimeMessage(CAPTURE_TIME - capTimer);
                if(capTimeMessage != null && !capTimeMessage.isEmpty())
                    Bukkit.broadcastMessage(capTimeMessage.replace("{PLAYER}", cappingPlayer.getName()).replace("{KOTH_NAME}", getName()).replace("{TIME_LEFT}", CAPTURE_TIME - capTimer + ""));
                
                if(NexGenKoths.useScoreboard)
                    NexGenKoths.globalScoreboardsMap.put(ChatColor.GREEN + name + " Cap", (int) (CAPTURE_TIME - capTimer));
                
                if(capTimer >= CAPTURE_TIME) {
                    playerCapturedKoth(player);
                    
                    stopCaptureTimer(player);
                }
            }
        }, 20, 20);
        
        if(broadcast)
        	Bukkit.broadcastMessage(NexGenKoths.kothCapStartMsg.replace("{KOTH_NAME}", getName()).replace("{PLAYER}", player.getName()));
    }
    
    
    public void stopCaptureTimer(Player player) {
    	stopCaptureTimer(player, true);
    }
    
    public void stopCaptureTimer(Player player, boolean broadcast) {
        Bukkit.getScheduler().cancelTask(capTimerID);
        capTimerID = -1;
        capTimer = 0;
        
        isBeingCapped = false;
        cappingPlayer = null;
        
        if(NexGenKoths.useScoreboard && NexGenKoths.globalScoreboardsMap.containsKey(ChatColor.GREEN + name + " Cap"))
            NexGenKoths.globalScoreboardsMap.remove(ChatColor.GREEN + name + " Cap");
        
        if(player == null)
            return;
        
        if(broadcast)
        	Bukkit.broadcastMessage(NexGenKoths.kothCapStopMsg.replace("{KOTH_NAME}", getName()).replace("{PLAYER}", player.getName()));
    }
    
    
    public void playerCapturedKoth(Player player) {
        PlayerCaptureKothEvent event = new PlayerCaptureKothEvent(player, this, new ArrayList<ItemStack>(), new HashMap<String, Double>());
        
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) return;
        
        stopKoth(false);
        
        for(ItemStack is : event.getLoot())
            InventoryUtils.givePlayerItemStack(player, is);
        
        for(Entry<String, Double> entry : event.getNonItemLoot().entrySet()) {
            switch(entry.getKey().toLowerCase()) {
            
            case "money":
                Vault.givePlayerMoney(player, entry.getValue());
                break;
            case "factionspower":
                Factions.addPower(player, entry.getValue());
                break;
            case "exp":
                player.giveExp(entry.getValue().intValue());
                break;
            default:
                Bukkit.getLogger().warning("Unknown non-item loot: " + entry.getKey());
                break;
            
            }
        }
        
        if(getFlagValue(KothFlag.BROADCAST_CAPTURE) != 0)
            Bukkit.broadcastMessage(NexGenKoths.kothCapturedMsg.replace("{KOTH_NAME}", getName()).replace("{PLAYER}", player.getName()));
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
    
    public Map<Long, String> getCapTimeMessages() {
        return Collections.unmodifiableMap(capTimeMessages);
    }
    
    public String getCapTimeMessage(Long time) {
        return capTimeMessages.get(time);
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
    
    public void setCapTimeMessages(Map<Long, String> capTimeMessages) {
        this.capTimeMessages = capTimeMessages;
    }
    
    public void addCapTimeMessage(Long time, String message) {
        capTimeMessages.put(time, message);
    }
    
    public void removeCapTimeMessage(Long time) {
        capTimeMessages.remove(time);
    }
    
    
}
