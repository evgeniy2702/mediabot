package ua.ukrposhta.mediabot.telegram.google;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.model.User;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@PropertySource("classpath:properties/google.properties")
public class GoogleSheetsLive {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    private SheetsServiceUtil sheetsServiceUtil;

    @Autowired
    public void setSheetsServiceUtil(SheetsServiceUtil sheetsServiceUtil) {
        this.sheetsServiceUtil = sheetsServiceUtil;
    }

    @Value("${google.spreadsheet.id}")
    private String SPREADSHEET_ID ;


    public Integer writeDataInExcelSheet(BotContext context,
                                         Integer numberOfCellExcelSheetForMediaRequest,
                                         Integer numberOfRowsForMediaRequest) throws IOException, GeneralSecurityException {

        consoleLogger.info("start writeDataInExcelSheet method in GoogleSheetsLive.class");

        List<ValueRange> data = new ArrayList<>();

        User user = context.getUser();

        if(numberOfCellExcelSheetForMediaRequest == numberOfRowsForMediaRequest)
            numberOfCellExcelSheetForMediaRequest = 2;

        ValueRange requestMediaInfo = new ValueRange()
                .setRange("A" + numberOfCellExcelSheetForMediaRequest)
                .setValues(Collections.singletonList(
                        Arrays.asList(LocalDate.now().toString(),user.getMediaName(), user.getName_surname(), user.getSubject(),
                                user.getPhone(), user.getEmail())));

        data.add(requestMediaInfo);

        ValueRange numberOfCellExcel = new ValueRange()
                .setRange("X994")
                .setValues(Collections.singletonList(
                        Collections.singletonList(numberOfCellExcelSheetForMediaRequest + 1)));

        data.add(numberOfCellExcel);

        telegramLogger.info("requestMediaInfo for write to excel sheet request from media : " + requestMediaInfo.toString());
        telegramLogger.info("write number of cell excel in cache as sheet : " + numberOfCellExcel.toString());

        try {

            BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                    .setValueInputOption("USER_ENTERED")
                    .setData(data);

            BatchUpdateValuesResponse batchResult = sheetsServiceUtil.getSheetsService().spreadsheets().values()
                    .batchUpdate(SPREADSHEET_ID, batchBody)
                    .execute();

        }catch (GoogleJsonResponseException google){
            consoleLogger.error("ERROR writeDataInExcelSheet method in GoogleSheetsLive.class : " + Arrays.toString(google.getStackTrace()));
            telegramLogger.error("ERROR writeDataInExcelSheet method in GoogleSheetsLive.class : " + Arrays.toString(google.getStackTrace()));
            google.getStackTrace();
        }

        return numberOfCellExcelSheetForMediaRequest + 1;
    }


    public Integer readNumberOfCellExcelSheetFromExcelSheet(BotContext context, int numberOfCellExcelSheet) throws IOException, GeneralSecurityException {

        consoleLogger.info("start readNumberOfCellExcelSheetFromExcelSheet method in GoogleSheetsLive.class");

        int numberOfRowsForMediaRequest = 100;

        try {
            List<String> ranges = Arrays.asList("X994","X992");
            BatchGetValuesResponse readResult = sheetsServiceUtil.getSheetsService().spreadsheets().values()
                    .batchGet(SPREADSHEET_ID)
                    .setRanges(ranges)
                    .execute();

            numberOfCellExcelSheet = Integer.valueOf(readResult.getValueRanges().get(0)
                                            .getValues().get(0).get(0).toString());

            numberOfRowsForMediaRequest = Integer.valueOf(readResult.getValueRanges().get(1)
                                            .getValues().get(0).get(0).toString());

            telegramLogger.info("body for write to excel sheet numberOfCellExcelSheet : " + numberOfCellExcelSheet + " ; numberOfRowsForMediaRequest : " + numberOfRowsForMediaRequest);
        }catch (GoogleJsonResponseException google){
            consoleLogger.error("ERROR readNumberOfCellExcelSheetFromExcelSheet method in GoogleSheetsLive.class : " + Arrays.toString(google.getStackTrace()));
            telegramLogger.error("ERROR readNumberOfCellExcelSheetFromExcelSheet method in GoogleSheetsLive.class : " + Arrays.toString(google.getStackTrace()));
            google.getStackTrace();
        }

        if(numberOfCellExcelSheet == 0)
            numberOfCellExcelSheet = 2;

        return writeDataInExcelSheet(context, numberOfCellExcelSheet, numberOfRowsForMediaRequest);
    }

}
