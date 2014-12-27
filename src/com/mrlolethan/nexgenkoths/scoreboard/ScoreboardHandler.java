package com.mrlolethan.nexgenkoths.scoreboard;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.P;

public class ScoreboardHandler {
	
	private Set<NexGenBoard> boards = new HashSet<NexGenBoard>();
	
	
	public ScoreboardHandler() {
		new BukkitRunnable() {
			public void run() {
				for(NexGenBoard board : boards) {
					board.update();
				}
			}
		}.runTaskTimer(P.p, NexGenKoths.scoreboardUpdateFrequency, NexGenKoths.scoreboardUpdateFrequency);
	}
	
	
	public void update(Player player) {
		if(hasBoard(player))
			getBoard(player).update();
	}
	
	
	public NexGenBoard getBoard(Player player) {
		NexGenBoard board = null;
		
		for(NexGenBoard b : boards) {
			if(b.getPlayer().equals(player))
				board = b;
		}
		
		return board;
	}
	
	public boolean hasBoard(Player player) {
		return getBoard(player) != null;
	}
	
	
	public void removeBoard(Player player) {
		if(!hasBoard(player)) return;
		
		boards.remove(getBoard(player));
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	public void addBoard(Player player) {
		if(hasBoard(player)) return;
		
		boards.add(new NexGenBoard(player));
	}
	
	
}
