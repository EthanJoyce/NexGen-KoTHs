package com.mrlolethan.nexgenkoths.scoreboard;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.mrlolethan.nexgenkoths.NexGenKoths;
import com.mrlolethan.nexgenkoths.koth.Koth;
import com.mrlolethan.nexgenkoths.scoreboard.NexGenPlayerScores.PlayerScoreGetter;
import com.mrlolethan.nexgenkoths.util.TimeUtils;

public class NexGenBoard {
	
	private Player player;
	private Objective objective;
	private Set<String> scores = new HashSet<String>();
	
	public NexGenBoard(Player player) {
		this.player = player;
		
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		objective = scoreboard.registerNewObjective("NexGen_KoTHs_SB", "dummy");
		objective.setDisplayName(NexGenKoths.scoreboardObjDisplayName);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		player.setScoreboard(scoreboard);
		
		update();
	}
	
	
	public void update() {
		int order = 15;
		for(PlayerScoreGetter scoreGetter : NexGenPlayerScores.ALL) {
			final int seconds = scoreGetter.getScore(player);
			final String title = scoreGetter.getTitle();
			
			if(seconds == -1) {
				if(scores.contains(title)) {
					objective.getScoreboard().resetScores(title);
					scores.remove(title);
				}
			} else {
				scores.add(title);
				objective.getScore(title).setScore(order--);
				getTeam(title, seconds).addPlayer(Bukkit.getOfflinePlayer(title));
			}
		}
		
		for(Koth koth : NexGenKoths.loadedKoths) {
			if(true) {
				KothEndScoreGetter scoreGetter = new KothEndScoreGetter(koth);
				
				final int seconds = scoreGetter.getScore();
				final String title = scoreGetter.getTitle();
				
				if(seconds == -1) {
					if(scores.contains(title)) {
						objective.getScoreboard().resetScores(title);
						scores.remove(title);
					}
				} else {
					scores.add(title);
					objective.getScore(title).setScore(order--);
					getTeam(title, seconds).addPlayer(Bukkit.getOfflinePlayer(title));
				}
			}
			
			if(true) {
				KothCaptureScoreGetter scoreGetter = new KothCaptureScoreGetter(koth);
				
				final int seconds = scoreGetter.getScore();
				final String title = scoreGetter.getTitle();
				
				if(seconds == -1) {
					if(scores.contains(title)) {
						objective.getScoreboard().resetScores(title);
						scores.remove(title);
					}
				} else {
					scores.add(title);
					objective.getScore(title).setScore(order--);
					getTeam(title, seconds).addPlayer(Bukkit.getOfflinePlayer(title));
				}
			}
		}
	}
	
	
	public Team getTeam(String title, int seconds) {
		final String name = ChatColor.stripColor(title);
		Team team = objective.getScoreboard().getTeam(name);
		if(team == null)
			team = objective.getScoreboard().registerNewTeam(name);
		team.setSuffix(ChatColor.GRAY + ": " + ChatColor.RED + TimeUtils.formatToMMSS(seconds)); // TODO make configurable.
		return team;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
	public Objective getObjective() {
		return objective;
	}
	
	public Set<String> getScores() {
		return scores;
	}
	
	
}
