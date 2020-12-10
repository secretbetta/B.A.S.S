package com.secretbetta.PrivateVC;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Secretbeta
 */
public class PrivateVCEvent extends ListenerAdapter {
	
	/* User ID : Voice channel ID */
	public static HashMap<String, String> users = new HashMap<>();
	
	public static EnumSet<Permission> allow = EnumSet.of(Permission.VOICE_CONNECT);
	
	public PrivateVCEvent() {
		allow.add(Permission.VOICE_SPEAK);
		allow.add(Permission.VOICE_STREAM);
		allow.add(Permission.VOICE_USE_VAD);
	}
	
	/**
	 * Deletes a message after n minutes.
	 * 
	 * @param message The Message to be removed
	 * @param minutes Time in minutes to be removed
	 */
	public static void deleteMessageTime(Message message, int minutes) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					message.delete().queue();
				}
			},
			1000 * minutes * 60);
	}
	
	/**
	 * Creates a new VC based on user who joins and moves them there
	 */
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		if (event.getMember().getUser().isBot()) {
			return;
		}
		
		if (event.getChannelJoined().getId().equals("786119690144710656")) {
			Member member = event.getMember();
			Guild guild = event.getGuild();
			User user = member.getUser();
			
			deleteMessageTime(user.openPrivateChannel().complete()
				.sendMessage("Use ~~vcadd <@user> to allow users to join the private VC!")
				.complete(), 1);
			
			Category cat = guild.getCategoryById("583562618044678171");
			String vcname = "🔒" + (member.getNickname() == null
				? member.getEffectiveName()
				: member.getNickname());
			vcname += "'s Private VC";
			
			// Denies @everyone permission to do anything to voice channel except view it
			// Gives host all permissions for VC (except server deafen and server mute)
			EnumSet<Permission> hostallow = allow;
			hostallow.add(Permission.VOICE_MOVE_OTHERS);
			
			VoiceChannel newVC = guild.createVoiceChannel(vcname, cat)
				.addRolePermissionOverride(Long.parseLong("583562618044678165"),
					Permission.VIEW_CHANNEL.getRawValue(),
					Permission.ALL_PERMISSIONS)
				.addMemberPermissionOverride(member.getIdLong(), hostallow,
					null)
				.complete();
			
			guild.moveVoiceMember(member, newVC).queue();
			users.put(member.getId(), newVC.getId());
		}
	}
	
	/**
	 * Deletes VC if host leaves it
	 */
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		if (event.getMember().getUser().isBot()) {
			return;
		}
		
		Member member = event.getMember();
		Guild guild = event.getGuild();
		String memberID = member.getId();
		
		if (users.containsKey(memberID)) {
			VoiceChannel vc = guild.getVoiceChannelById(users.get(memberID));
			vc.delete().queue();
			users.remove(memberID);
		}
	}
	
	/**
	 * Deletes VC if host leaves it
	 */
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		if (event.getMember().getUser().isBot()) {
			return;
		}
		
		Member member = event.getMember();
		Guild guild = event.getGuild();
		String memberID = member.getId();
		
		if (users.containsKey(memberID)
			&& !users.get(memberID).equals(event.getChannelJoined().getId())) {
			VoiceChannel vc = guild.getVoiceChannelById(users.get(memberID));
			vc.delete().queue();
			users.remove(memberID);
		}
	}
	
	/**
	 * Allows a user to join the private voice channel
	 * 
	 * @author Secretbeta
	 */
	public class PrivateVCAdd extends Command {
		
		public PrivateVCAdd() {
			super.name = "vcadd";
			super.help = "Allows user to join VC";
			super.cooldown = 3;
		}
		
		@Override
		protected void execute(CommandEvent event) {
			List<Member> members = event.getMessage().getMentionedMembers();
			Consumer<Message> msgdelete = msg -> PrivateVCEvent.deleteMessageTime(msg, 1);
			if (members.size() < 1) {
				event.reply("No member mentioned. Usage:\\n~~vcadd <@user>", msgdelete);
				return;
			}
			
			if (users.containsKey(event.getAuthor().getId())) {
				Guild guild = event.getGuild();
				User user = event.getAuthor();
				VoiceChannel vc = guild
					.getVoiceChannelById(users.get(user.getId()));
				
				vc.putPermissionOverride(members.get(0))
					.grant(allow).queue();
				
				event.reply("User has been added", msgdelete);
			} else {
				event.reply("You need to host a room to use this command", msgdelete);
			}
		}
	}
	
	/**
	 * Changes the host of the private VC channel
	 * 
	 * @author Secretbeta
	 */
	public class PrivateVCChangeHost extends Command {
		
		public PrivateVCChangeHost() {
			super.name = "vchost";
			super.help = "Change host";
			super.aliases = new String[] { "host" };
		}
		
		@Override
		protected void execute(CommandEvent event) {
			List<Member> members = event.getMessage().getMentionedMembers();
			Consumer<Message> msgdelete = msg -> PrivateVCEvent.deleteMessageTime(msg, 1);
			if (members.size() >= 1) {
				// user needs to be a host to call this command
				if (!users.containsKey(event.getMember().getId())) {
					event.reply("You must be a host to use this command");
					return;
				}
				
				Guild guild = event.getGuild();
				User user = event.getAuthor();
				
				VoiceChannel vc = guild
					.getVoiceChannelById(users.get(user.getId()));
				
				// If member is not in the vc, don't give them host
				List<Member> vcmembers = vc.getMembers();
				if (!vcmembers.contains(members.get(0))) {
					event.reply("Member must join the VC to become a host", msgdelete);
					return;
				}
				
				vc.putPermissionOverride(members.get(0)).grant(Permission.ALL_VOICE_PERMISSIONS);
				vc.putPermissionOverride(event.getMember())
					.deny(Permission.ALL_VOICE_PERMISSIONS).grant(allow);
				
				String vcID = users.get(event.getMember().getId());
				users.remove(event.getMember().getId());
				users.put(members.get(0).getId(), vcID);
				
				event.reply("Host has been changed to " + members.get(0).getAsMention(), msgdelete);
			} else {
				event.reply("No member mentioned. Usage:\n~~host <@user>", msgdelete);
			}
		}
	}
	
	/**
	 * Allows host to change the name of the VC channel
	 * 
	 * @author Secretbeta
	 */
	public class PrivateVCChangeName extends Command {
		
		public PrivateVCChangeName() {
			super.name = "vcname";
			super.help = "change the name of the VC room";
			super.arguments = "<name>";
		}
		
		@Override
		protected void execute(CommandEvent event) {
			Consumer<Message> msgdelete = msg -> PrivateVCEvent.deleteMessageTime(msg, 1);
			if (users.containsKey(event.getMember().getId())) {
				Guild guild = event.getGuild();
				User user = event.getAuthor();
				String name = event.getArgs();
				
				VoiceChannel vc = guild
					.getVoiceChannelById(users.get(user.getId()));
				vc.getManager().setName(name);
			} else {
				event.reply("You need to be the host of the room to change its name", msgdelete);
				return;
			}
			
		}
		
	}
}
