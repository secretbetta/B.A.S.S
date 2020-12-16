package com.secretbetta.BASS.VC;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Notifies the server who joined/left which voice channel.
 * 
 * @author Secretbeta
 */
public class VCNotificationEvent extends ListenerAdapter {
	
	private String channelID = "621805408725630977";
	
	Map<String, Message> joins = new HashMap<>();
	
	/**
	 * Cooldown until alert is deleted.
	 * 
	 * @param channel Which channel to send alert
	 * @param minutes Minutes before removing alert
	 * @param id      Alert to delete
	 */
	public void timer(MessageChannel channel, int minutes, String id) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					joins.get(id).delete().queue();
					joins.remove(id);
				}
			},
			1000 * minutes * 60);
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		VoiceChannel channelJoin = event.getChannelJoined();
		Member member = event.getMember();
		
		TextChannel notificationalChannel = event.getJDA()
			.getTextChannelById(this.channelID);
		
		if (notificationalChannel == null) {
			return;
		}
		
		String channelName = channelJoin.getName();
		
		if (joins.containsKey(member.getId())) {
			Message msg = joins.get(member.getId());
			msg.editMessage(
				"User " + member.getEffectiveName() + " has joined channel " + channelName + ".")
				.queue();
		} else { // Does not contain key
			joins.put(member.getId(), notificationalChannel.sendMessage(
				"User " + member.getEffectiveName() + " has joined channel " + channelName + ".")
				.complete());
			timer(notificationalChannel, 5, member.getId());
		}
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel channelLeave = event.getChannelLeft();
		Member member = event.getMember();
		TextChannel notificationalChannel = event.getJDA().getTextChannelById(this.channelID);
		String channelName = channelLeave.getName();
		
		if (channelLeave.getId().equals("774389937633099786")) {
			return;
		}
		
		if (joins.containsKey(member.getId())) {
			Message msg = joins.get(member.getId());
			msg.editMessage(
				"User " + member.getEffectiveName() + " has left channel " + channelName + ".")
				.queue();
		} else {
			joins.put(member.getId(),
				notificationalChannel.sendMessage(
					"User " + member.getEffectiveName() + " has left channel " + channelName + ".")
					.complete());
			timer(notificationalChannel, 1, member.getId());
		}
		
	}
}
