package com.mrlolethan.nexgenkoths.scoreboard;

import org.bukkit.ChatColor;

import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.koth.KothFlag;

public class KothEndScoreGetter extends KothScoreGetter {
	
	public KothEndScoreGetter(Koth koth) {
		super(koth);
	}
	
	
	public String getTitle() {
		return ChatColor.YELLOW + koth.getName() + " Ends";
	}
	
	
	public int getScore() {
		int score = -1;
		
		if(koth.isActive() && koth.getFlagValue(KothFlag.AUTO_END) != 0)
			score = (int) koth.getAutoEndTimer();
		
		return score;
	}
	
	
}
