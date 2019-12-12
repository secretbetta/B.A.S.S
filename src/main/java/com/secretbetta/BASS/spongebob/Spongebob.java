package com.secretbetta.BASS.spongebob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

public class Spongebob {
	
	private HashSet<String> trollIds = new HashSet<>();
	
	String path = "Troll_IDs";
	
	/**
	 * Adds default trolled users
	 * @throws IOException 
	 */
	public Spongebob() throws IOException {
//		this.trollIds.add("268480279746838529"); // Sun's id
//		trollIds.add("400805008276193290"); // Justine's id
		this.loadUsers();
	}
	
	/**
	 * Changes id in list. Adds if it doesn't exist, removes if it does
	 * 
	 * @param id User ID from discord
	 * @return 1 if added user, 0 if removed user
	 * @throws IOException 
	 */
	public int changeList(String id) throws IOException {
		if (this.trollIds.contains(id)) {
			this.removeUser(id);
			this.loadUsers();
			return 0;
		} else {
			this.addUser(id);
			this.loadUsers();
			return 1;
		}
	}
	
	/**
	 * Loads all user IDs from text file
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void loadUsers() throws IOException {
		BufferedReader reader = Files.newBufferedReader(Paths.get(this.path), Charset.forName("ISO-8859-1"));
		this.trollIds = new HashSet<>();
		String line = "";
		while ((line = reader.readLine()) != null) {
			this.trollIds.add(line);
		}
	}
	
	/**
	 * Adds id of User to list of trolled people
	 * 
	 * @param id String id from Discord
	 * @throws IOException 
	 */
	public void addUser(String id) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.path, true));
		writer.append(id + "\n");
		writer.close();
	}
	
	/**
	 * Removes id of User from list of trolled people
	 * 
	 * @param id String id from Discord
	 * @throws IOException 
	 */
	public void removeUser(String id) throws IOException {
		File temp = new File("tempfile.txt");
		BufferedReader reader = Files.newBufferedReader(Paths.get(this.path), Charset.forName("ISO-8859-1"));
		BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			String trimmedLine = line.trim();
			if (trimmedLine.equals(id)) continue;
			writer.write(trimmedLine + System.getProperty("line.separator"));
		}
		reader.close();
		writer.close();
		temp.renameTo(new File(this.path));
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
	
	public static void main(String[] args) throws IOException {
		Spongebob test = new Spongebob();
		
		test.loadUsers();
		
		test.addUser("400805008276193290");
		test.addUser("260088750636400641");
		test.addUser("349383349816786944");
		System.out.println(test.trollIds);
		test.changeList("260088750636400641");
		System.out.println(test.trollIds);
		test.removeUser("260088750636400641");
		System.out.println(test.trollIds);
	}
}
