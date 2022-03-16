package ua.ukrposhta.mediabot.telegram.model.readers;

import org.springframework.stereotype.Component;
import ua.ukrposhta.mediabot.telegram.model.button_text.MessagePayload;
import ua.ukrposhta.mediabot.telegram.model.button_text.MessagePayloadList;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import javax.ws.rs.NotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Optional;

@Component
public class MessagePayloadReader {

    private static MessagePayloadReader instance;
    private MessagePayloadList messagePayloadList;
    private static BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);

    private MessagePayloadReader() throws JAXBException, NullPointerException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MessagePayloadList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        File messageXMLFile = new File(getClass().getClassLoader().getResource("messages/messages.xml").getFile());
        messagePayloadList = (MessagePayloadList) unmarshaller.unmarshal(messageXMLFile);
    }

    public static MessagePayloadReader getInstance() throws JAXBException {

        consoleLogger.info("start getInstance method in MessagePayloadReader.class");

        if (instance == null) {
            instance = new MessagePayloadReader();
        }
        return instance;
    }

    public MessagePayload getMessagePayload(String messagePayload) {

        consoleLogger.info("start getMessagePayload method in MessagePayloadReader.class");

        telegramLogger.info("messagePayload : " + messagePayload);

        Optional<MessagePayload> messagePayloadOptional = messagePayloadList
                .getMessagePayloads()
                .stream()
                .filter(msg ->
                        msg.getName().equalsIgnoreCase(messagePayload))
                .findFirst();

        return messagePayloadOptional.orElseThrow(NotFoundException::new);
    }

    public boolean checkMatcheStandardMessage(String text){
        return messagePayloadList.getMessagePayloads().stream().anyMatch(msg -> msg.getName().equalsIgnoreCase(text));
    }
}

