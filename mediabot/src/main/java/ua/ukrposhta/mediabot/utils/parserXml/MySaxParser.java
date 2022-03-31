package ua.ukrposhta.mediabot.utils.parserXml;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import ua.ukrposhta.mediabot.telegram.model.MessagesListBot;
import ua.ukrposhta.mediabot.utils.handlerXml.MySaxHandler;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Component
public class MySaxParser {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);


    public MessagesListBot getObjectExchangeFromXML(String uri, MySaxHandler mySaxHandler) throws ParserConfigurationException, SAXException, IOException {

        consoleLogger.info("start getObjectExchangeFromXML method in MySaxParser.class");

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            File xmlFile = new File(getClass().getClassLoader().getResource(uri).getFile());
            telegramLogger.info("File exists = " + xmlFile.exists() + " |  File can read = " + xmlFile.canRead() +
                    " |  File path = " + xmlFile.getAbsolutePath());
            parser.parse(xmlFile, mySaxHandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            telegramLogger.error("ERROR : " + Arrays.toString(e.getStackTrace()));
        }

        return mySaxHandler.getMessages();
    }
}
