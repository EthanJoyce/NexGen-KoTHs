package com.mrlolethan.nexgenkoths.scoreboard;

import org.bukkit.ChatColor;

import com.mrlolethan.nexgenkoths.koth.Koth;

public class KothCaptureScoreGetter extends KothScoreGetter {
	
	public KothCaptureScoreGetter(Koth koth) {
		super(koth);
	}
	
	
	public String getTitle() {
		return ChatColor.YELLOW + koth.getName() + " Cap";
	}
	
	
	public int getScore() {
		int score = -1;
		
		if(koth.isBeingCaptured())
			score = (int) koth.getCaptureTimer();
		
		return score;
	}
	
	
}
