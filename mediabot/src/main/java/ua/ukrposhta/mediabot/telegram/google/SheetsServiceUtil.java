package ua.ukrposhta.mediabot.telegram.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@PropertySource("classpath:properties/google.properties")
public class SheetsServiceUtil {

    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private static BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);

    @Value("${google.application.name}")
    private String APPLICATION_NAME = "MediaBot";
    @Value("${google.credentials.file.path}")
    private static String credentials_file_path = "/google-sheets-client-secret.json";
    @Value("${google.tokens.directory.path}")
    private static String tokens_directory_path = "tokens";

    public Sheets getSheetsService() throws IOException {

        consoleLogger.info("start getSheetsService method in SheetsServiceUtil.class");

        InputStream in = SheetsServiceUtil.class.getResourceAsStream(credentials_file_path);
        if (in == null) {

            telegramLogger.error("Resource not found: " + credentials_file_path);

            throw new FileNotFoundException("Resource not found: " + credentials_file_path);
        }

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, SheetsScopes.DRIVE_FILE);

        Sheets sheets = null;

        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(in).createScoped(scopes);
            sheets = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(googleCredentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

        } catch (Exception e){

            consoleLogger.error("ERROR in authorize method in GoogleAuthorizeUtil.class : Sheets not builder .");
            telegramLogger.error("ERROR in authorize method in GoogleAuthorizeUtil.class : Sheets not builder .");

        }

        return sheets;

    }
}
