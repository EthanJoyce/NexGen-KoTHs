package org.mle.nexgenkoths.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.mle.nexgenkoths.NexGenKoths;

public enum ScoreboardUtil {;
    
    public static void updateScoreboard(Player player, Map<String, Integer> map) {
       Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
       if(player.getScoreboard() != null)
           board = player.getScoreboard();
       
       Objective obj = board.getObjective("nexgen_koths");
       
       if(obj == null) {
           obj = board.registerNewObjective("nexgen_koths", "dummy");
           obj.setDisplaySlot(DisplaySlot.SIDEBAR);
           obj.setDisplayName(NexGenKoths.scoreboardObjDisplayName);
       } else {
           obj.unregister();
           
           obj = board.registerNewObjective("nexgen_koths", "dummy");
           obj.setDisplaySlot(DisplaySlot.SIDEBAR);
           obj.setDisplayName(NexGenKoths.scoreboardObjDisplayName);
       }
       
       Map<String, Integer> globalScoreboardsMapCopy = new HashMap<String, Integer>(NexGenKoths.globalScoreboardsMap);
       for(Entry<String, Integer> entry : globalScoreboardsMapCopy.entrySet()) {
           if(entry.getValue() > 0)
               obj.getScore(entry.getKey()).setScore(entry.getValue());
           else {
               board.resetScores(entry.getKey());
               Bukkit.getServer().broadcastMessage(entry.getKey());
               NexGenKoths.globalScoreboardsMap.remove(entry.getKey());
           }
       }
       
       if(map != null) {
           for(Entry<String, Integer> entry : map.entrySet()) {
               if(entry.getValue() > 0)
                   obj.getScore(entry.getKey()).setScore(entry.getValue());
               else {
                   board.resetScores(entry.getKey());
                   map.remove(entry.getKey());
               }
           }
       }
       
       player.setScoreboard(board);
    }
    
    
    public static void clearScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
    
    
}
