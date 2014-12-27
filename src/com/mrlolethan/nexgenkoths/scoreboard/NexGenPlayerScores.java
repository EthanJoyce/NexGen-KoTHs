package com.mrlolethan.nexgenkoths.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.mrlolethan.nexgenkoths.NexGenKoths;

public enum NexGenPlayerScores {;
	
	static final PlayerScoreGetter CAPTURE_COOLDOWN = new PlayerScoreGetter() {
		public String getTitle() {
			return ChatColor.GREEN + "Cap Cooldown";
		}
		
		public int getScore(Player player) {
			int score = -1;
			
			if(NexGenKoths.isOnCaptureCooldown(player))
				score =(int) NexGenKoths.getCaptureCooldownRemaining(player);
			
			return score;
		}
	};
	
	static PlayerScoreGetter[] ALL = new PlayerScoreGetter[] { CAPTURE_COOLDOWN };
	
	
	interface PlayerScoreGetter {
		String getTitle();
		int getScore(Player player);
	}
	
}
