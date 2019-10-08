import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * Readers LeaderboardSheet from Google Spreadsheets
 * @author Andrew
 *
 */
public class LeaderboardSheet {
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    
    private final String spreadsheetId;
    private final NetHttpTransport HTTP_TRANSPORT;
    private Sheets service;
    
    /**
     * Initializes which spreadsheet to use and the Api Client
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public LeaderboardSheet() throws GeneralSecurityException, IOException {
    	this.spreadsheetId = "1-f49unMhxyZ0w_R4RR6i4Rcx_d8QoENDHLSaYmni5Wg";
	    this.HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    this.service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = LeaderboardSheet.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    /**
     * Gets Leaderboard from Main Leaderboard Sheet
     * 
     * @param spreadsheetId ID of Google spreadsheet
     * @param HTTP_TRANSPORT Authorized API client from google
     * 
     * @return Formatted String of Leaderboard
     * @throws IOException
     */
    public String getLeaderboard() throws IOException {
    	String range = "Main Leaderboard!A2:B20";
    	String content = "```";
    	
        ValueRange response = this.service.spreadsheets().values()
                .get(this.spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            content += "No data found.";
        } else {
        	// Makes Leaderboard
        	content += String.format("Player    : Total Wins\n");
            for (List<Object> row : values) {
                try {
                	content += String.format("%-10s: %3s\n", row.get(0), row.get(1));
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Index does not exist\n");
                }
            }
        }
        content += "```";
		return content;
    }
    
    /**
     * Get player vs. player score
     * 
     * @param name1 Name of player1
     * @param name2 Name of player2
     * @return Returns formatted string of scores between players
     * @throws IOException
     */
    public String getPlayer(String name1, String name2) throws IOException {
    	String range = name1 + "!A1:M50";
    	String content = "```css\n"; // CSS Markdown format
    	
    	ValueRange response = this.service.spreadsheets().values()
                .get(this.spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            content += "No data found.";
        } else {
        	// Finds player 2
        	List<Object> names = values.get(0);
        	int column = 0;
        	for (int i = 1; i < names.size(); i+=2) {
        		if (names.get(i).toString().toLowerCase().equals(name2)) {
        			column = i;
        		}
        	}
        	
        	// Prints scores
        	if (column != 0) {
	        	content += "[" + Character.toUpperCase(name1.charAt(0)) 
	        			+ name1.substring(1) 
	        			+ " Vs. " + Character.toUpperCase(name2.charAt(0)) 
	        			+ name2.substring(1) + "]\n\n";
	            for (List<Object> row : values) {
	                try {
	                	content += String.format("%-25s: %-3s,%3s\n", 
	                			row.get(0), row.get(column), row.get(column+1));
	                	if (row.get(column).equals("Wins")) {
	                		content += "\n--------------------------------------------";
	                	}
	                } catch (IndexOutOfBoundsException e) {
	                    System.err.println("Index does not exist\n");
	                }
	            }
        	} else {
        		return "Player 2 not found";
        	}
        }
    	return content + "```";
    }
    
    /**
     * Adds new player to a new sheet in leaderboard
     * @return
     */
    public boolean addPlayer() {
    	// TODO AddPlayer to leaderboard
    	// 1. Add to MainLeaderboard new row
    	// 2. Add new sheet to spreadsheet (copy from a template)
    	return false;
    }
}
