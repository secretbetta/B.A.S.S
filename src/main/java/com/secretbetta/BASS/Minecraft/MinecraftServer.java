package com.secretbetta.BASS.Minecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

/**
 * Edited by @author Andrew Nguyen
 * 
 * @author zh32 <zh32 at zh32.de>
 */
public class MinecraftServer {
	
	private InetSocketAddress host;
	private int timeout = 7000;
	private Gson gson;
	
	/**
	 * Default Constructor
	 */
	public MinecraftServer() {
		this.gson = new Gson();
	}
	
	/**
	 * Constructor for hostname and port number
	 * 
	 * @param hostname
	 * @param port
	 */
	public MinecraftServer(String hostname, int port) {
		this();
		this.setAddress(new InetSocketAddress(hostname, port));
	}
	
	/**
	 * Constructor for hostname, port number, and timeout in milliseconds
	 * 
	 * @param hostname
	 * @param port
	 * @param timeout
	 */
	public MinecraftServer(String hostname, int port, int timeout) {
		this();
		this.timeout = timeout;
		this.setAddress(new InetSocketAddress(hostname, port));
	}
	
	/**
	 * Sets IP Address
	 * 
	 * @param host
	 */
	public void setAddress(InetSocketAddress host) {
		this.host = host;
	}
	
	/**
	 * Gets IP Address
	 * 
	 * @return {@link #host}
	 */
	public InetSocketAddress getAddress() {
		return this.host;
	}
	
	/**
	 * Sets timeout in milliseconds
	 * 
	 * @param timeout
	 */
	void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Gets timeout in millieconds
	 * 
	 * @return {@link #timeout}
	 */
	int getTimeout() {
		return this.timeout;
	}
	
	public int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
			if ((k & 0x80) != 128) {
				break;
			}
		}
		return i;
	}
	
	public void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}
			
			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}
	
	/**
	 * Fetches Data from server
	 * 
	 * @return Data of server
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "unused" })
	public StatusResponse fetchData() throws IOException {
		
		Socket socket = new Socket();
		OutputStream outputStream;
		DataOutputStream dataOutputStream;
		InputStream inputStream;
		InputStreamReader inputStreamReader;
		
		socket.setSoTimeout(this.timeout);
		
		socket.connect(this.host, this.timeout);
		
		outputStream = socket.getOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);
		
		inputStream = socket.getInputStream();
		inputStreamReader = new InputStreamReader(inputStream);
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream handshake = new DataOutputStream(b);
		handshake.writeByte(0x00); // packet id for handshake
		this.writeVarInt(handshake, 4); // protocol version
		this.writeVarInt(handshake, this.host.getHostString().length()); // host length
		handshake.writeBytes(this.host.getHostString()); // host string
		handshake.writeShort(this.host.getPort()); // port
		this.writeVarInt(handshake, 1); // state (1 for handshake)
		
		this.writeVarInt(dataOutputStream, b.size()); // prepend size
		dataOutputStream.write(b.toByteArray()); // write handshake packet
		
		dataOutputStream.writeByte(0x01); // size is only 1
		dataOutputStream.writeByte(0x00); // packet id for ping
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		int size = this.readVarInt(dataInputStream); // size of packet
		int id = this.readVarInt(dataInputStream); // packet id
		
		if (id == -1) {
			throw new IOException("Premature end of stream.");
		}
		
		if (id != 0x00) { // we want a status response
			throw new IOException("Invalid packetID");
		}
		int length = this.readVarInt(dataInputStream); // length of json string
		
		if (length == -1) {
			throw new IOException("Premature end of stream.");
		}
		
		if (length == 0) {
			throw new IOException("Invalid string length.");
		}
		
		byte[] in = new byte[length];
		dataInputStream.readFully(in); // read json string
		String json = new String(in);
		
		long now = System.currentTimeMillis();
		dataOutputStream.writeByte(0x09); // size of packet
		dataOutputStream.writeByte(0x01); // 0x01 for ping
		dataOutputStream.writeLong(now); // time!?
		
		this.readVarInt(dataInputStream);
		id = this.readVarInt(dataInputStream);
		if (id == -1) {
			throw new IOException("Premature end of stream.");
		}
		
		if (id != 0x01) {
			throw new IOException("Invalid packetID");
		}
		long pingtime = dataInputStream.readLong(); // read response
		
		StatusResponse response = this.gson.fromJson(json, StatusResponse.class);
		
		dataOutputStream.close();
		outputStream.close();
		inputStreamReader.close();
		inputStream.close();
		socket.close();
		
		return response;
	}
	
	/**
	 * Full server detail
	 * 
	 * @author zh32 <zh32 at zh32.de>
	 *
	 */
	public class StatusResponse {
		
		private Description description;
		private Players players;
		private Version version;
		private String favicon;
		private int time;
		
		public Description getDescription() {
			return this.description;
		}
		
		public Players getPlayers() {
			return this.players;
		}
		
		public Version getVersion() {
			return this.version;
		}
		
		public String getFavicon() {
			return this.favicon;
		}
		
		public int getTime() {
			return this.time;
		}
		
		public void setTime(int time) {
			this.time = time;
		}
	}
	
	/**
	 * Players Info Class
	 * 
	 * @author zh32 <zh32 at zh32.de>
	 *
	 */
	public class Players {
		
		private int max;
		private int online;
		private List<Player> sample;
		
		public int getMax() {
			return this.max;
		}
		
		public int getOnline() {
			return this.online;
		}
		
		public List<Player> getSample() {
			return this.sample;
		}
	}
	
	/**
	 * Player Info Class
	 * 
	 * @author zh32 <zh32 at zh32.de>
	 *
	 */
	public class Player {
		
		private String name;
		private String id;
		
		public String getName() {
			return this.name;
		}
		
		public String getId() {
			return this.id;
		}
	}
	
	/**
	 * Server Version Class
	 * 
	 * @author zh32 <zh32 at zh32.de>
	 *
	 */
	public class Version {
		
		private String name;
		private int protocol;
		
		public String getName() {
			return this.name;
		}
		
		public int getProtocol() {
			return this.protocol;
		}
	}
	
	/**
	 * Server Description Class
	 * 
	 * @author zh32 <zh32 at zh32.de>
	 *
	 */
	public class Description {
		
		private String text;
		
		public String getText() {
			return this.text;
		}
	}
}