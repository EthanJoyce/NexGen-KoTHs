package org.mle.nexgenkoths.integration;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
    
    private static Economy econ;
    
    
    public static boolean setupEconomy() {
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null)
            return false;
        
        econ = rsp.getProvider();
        return econ != null;
    }
    
    
    public static void givePlayerMoney(Player player, double amt) {
        if(econ != null)
            econ.depositPlayer(player, amt);
    }
    
    
}
