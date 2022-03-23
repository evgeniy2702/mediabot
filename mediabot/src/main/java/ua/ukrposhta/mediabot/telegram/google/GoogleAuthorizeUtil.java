package ua.ukrposhta.mediabot.telegram.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@PropertySource("classpath:properties/google.properties")
public class GoogleAuthorizeUtil {

    @Value("${google.credentials.file.path}")
    private static String credentials_file_path = "/google-sheets-client-secret.json";

    @Value("${google.tokens.directory.path}")
    private static String tokens_directory_path = "tokens";

    private static BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private static BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    public static Credential authorize() throws IOException, GeneralSecurityException {

        consoleLogger.info("start authorize method in GoogleAuthorizeUtil.class");

        InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream(credentials_file_path);
        if (in == null) {

            consoleLogger.error("ERROR in authorize method in GoogleAuthorizeUtil.class : Resource not found: " + credentials_file_path);
            telegramLogger.error("Resource not found: " + credentials_file_path);

            throw new FileNotFoundException("Resource not found: " + credentials_file_path);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline").build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();

        Credential credential = null;
        try {
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (Exception e){
            e.getStackTrace();
        }

        return credential;
    }

}
