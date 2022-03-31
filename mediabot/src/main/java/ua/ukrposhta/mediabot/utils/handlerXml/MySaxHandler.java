package ua.ukrposhta.mediabot.utils.handlerXml;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.ukrposhta.mediabot.telegram.model.MessageBot;
import ua.ukrposhta.mediabot.telegram.model.MessagesListBot;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.util.ArrayList;

@Component
public class MySaxHandler extends DefaultHandler {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    private final String MESSAGES_TAG = "messages";
    private final String MESSAGE_TAG = "message";
    private final String TYPE_TAG = "type";
    private final String TXT_TAG = "txt";

    private MessagesListBot messages;
    private MessageBot messageBot;
    private String messageTag;

    @Override
    public void startDocument() throws SAXException {
        consoleLogger.info("start startDocument method in MySaxHandler.class");

        messages = new MessagesListBot();
    }

    public MessagesListBot getMessages() {
        return messages;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        consoleLogger.info("start startElement method in MySaxHandler.class");

        messageTag = qName;

        switch (messageTag){
            case MESSAGES_TAG: {
                messages.setMessages(new ArrayList<>());
            }
            case MESSAGE_TAG: messageBot = new MessageBot();
        }

        telegramLogger.info("startElement method MySaxHandler.class : messageTag - " + messageTag);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        consoleLogger.info("start startElement method in MySaxHandler.class");

        String text = new String(ch,start,length);

        if(text.contains("<") || messageTag == null){
            return;
        }
        switch (messageTag){
            case TYPE_TAG: messageBot.setType(text);
            case TXT_TAG: messageBot.setTxt(text);
        }

        telegramLogger.info("characters method MySaxHandler.class : text - " + text);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        consoleLogger.info("endElement startElement method in MySaxHandler.class");

        messageTag = qName;

        if (MESSAGE_TAG.equals(messageTag)) {
            messages.getMessages().add(messageBot);
            messageBot = null;
        }

        telegramLogger.info("endElement method MySaxHandler.class : messageTag - " + messageTag);

        messageTag = null;
    }
}
