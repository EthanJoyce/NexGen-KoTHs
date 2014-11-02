package org.mle.nexgenkoths.integration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.UPlayer;

public enum Factions {;
    
    public static void addPower(Player player, double amt) {
        if(!isFactionsLoaded()) return;
        
        UPlayer uplayer = UPlayer.get(player);
        uplayer.setPower(uplayer.getPower() + amt);
    }
    
    
    public static boolean isFactionsLoaded() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Factions");
    }
    
    
}
