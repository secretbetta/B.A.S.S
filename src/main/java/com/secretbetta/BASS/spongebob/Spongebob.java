package com.secretbetta.BASS.spongebob;

import java.util.HashSet;

public class Spongebob {
	
	private HashSet<String> trollIds = new HashSet<>();
	
	/**
	 * Adds default trolled users
	 */
	public Spongebob() {
		this.trollIds.add("268480279746838529"); // Sun's id
		// trollIds.add("400805008276193290"); // Justine's id
	}
	
	/**
	 * Changes id in list. Adds if it doesn't exist, removes if it does
	 * 
	 * @param id User ID from discord
	 * @return 1 if added user, 0 if removed user
	 */
	public int changeList(String id) {
		if (this.addUser(id)) {
			return 1;
		} else {
			this.removeUser(id);
			return 0;
		}
	}
	
	/**
	 * Adds id of User to list of trolled people
	 * 
	 * @param id String id from Discord
	 * @return True if user does not exist in {@link #trollIds}
	 */
	public boolean addUser(String id) {
		return this.trollIds.add(id);
	}
	
	/**
	 * Removes id of User from list of trolled people
	 * 
	 * @param id String id from Discord
	 * @return True if user exists and is removed from {@link #trollIds}
	 */
	public boolean removeUser(String id) {
		return this.trollIds.remove(id);
	}
	
	/**
	 * ManIpuLAtES STrINg tO RaNdom UPperCasE aND LoWErcAse WordS
	 * 
	 * @param content Content to manipulate
	 * @return Manipulated String
	 */
	public static String spongebobUpper(String content) {
		String newContent = "";
		
		for (int i = 0; i < content.length(); i++) {
			newContent += (int) (Math.random() * 2) == 0 ? Character.toLowerCase(content.charAt(i))
				: Character.toUpperCase(content.charAt(i));
		}
		
		return newContent;
	}
}
