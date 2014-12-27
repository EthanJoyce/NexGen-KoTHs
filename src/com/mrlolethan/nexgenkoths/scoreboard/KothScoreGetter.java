package com.mrlolethan.nexgenkoths.scoreboard;

import com.mrlolethan.nexgenkoths.Koth;

public abstract class KothScoreGetter {
	
	protected Koth koth;
	
	public KothScoreGetter(Koth koth) {
		this.koth = koth;
	}
	
	
	public abstract String getTitle();
	public abstract int getScore();
	
}
