package ua.ukrposhta.mediabot.telegram.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@PropertySource("classpath:properties/google.properties")
public class SheetsServiceUtil {

    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    @Value("${google.application.name}")
    private String APPLICATION_NAME ;

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {

        consoleLogger.info("start getSheetsService method in SheetsServiceUtil.class");

        Credential credential = GoogleAuthorizeUtil.authorize();

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
