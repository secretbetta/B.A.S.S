package com.secretbetta.BASS.Driver;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class MusicTest {
	
	private AudioPlayerManager playerManager;
	
	public MusicTest() {
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
	}
	
	public void runner() {
		AudioPlayer player = playerManager.createPlayer();
		// TrackScheduler trackScheduler = new TrackScheduler(player);
		// player.addListener(trackScheduler);
	}
}
