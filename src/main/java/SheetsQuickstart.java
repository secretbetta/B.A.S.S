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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class SheetsQuickstart {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
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
     * @param spreadsheetId ID of Google spreadsheet
     * @param HTTP_TRANSPORT Authorized API client from google
     * @return Formatted list
     * @throws IOException
     */
    public static String getLeaderboard(String spreadsheetId, NetHttpTransport HTTP_TRANSPORT) throws IOException {
    	String range = "Main Leaderboard!A1:B10";
        
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name Colors, Total Wins");
            for (List<Object> row : values) {
                // Print #Range, which correspond to indices 0 and 1.
                try {
                    System.out.printf("%s, %s\n", row.get(0), row.get(1));
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Index does not exist\n");
                }
            }
        }
		return null;
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // spreadsheet ID is the gibberish between /d/ and /edit
        // Ex: https://docs.google.com/spreadsheets/d/1-f49unMhxyZ0w_R4RR6i4Rcx_d8QoENDHLSaYmni5Wg/edit#gid=1993114807
        final String spreadsheetId = "1-f49unMhxyZ0w_R4RR6i4Rcx_d8QoENDHLSaYmni5Wg";
        // Range of <Name of Sheet>!<Rows & Columns range>
        String range = "Main Leaderboard!A1:B10";
        
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name Colors, Total Wins");
            for (List<Object> row : values) {
                // Print #Range, which correspond to indices 0 and 1.
                try {
                    System.out.printf("%s, %s\n", row.get(0), row.get(1));
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Index does not exist\n");
                }
            }
        }
        System.out.println();

        // Lists Andrew's Scores vs Sven
        range = "Andrew!A5:C20";
        response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.printf("%-25s|%-2s|%2s\n", "Games", "Wins", "Losses");
            for (List<Object> row : values) {
                // Print #Range, which correspond to indices 0 and 1.
                try {
                    System.out.printf("%-25s: %-3s, %3s\n", 
                        row.get(0), 
                        row.get(1), 
                        row.get(2));
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Index does not exist\n");
                }
            }
        }
    }
}