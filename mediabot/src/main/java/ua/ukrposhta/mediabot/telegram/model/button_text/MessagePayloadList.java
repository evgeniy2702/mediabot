package ua.ukrposhta.mediabot.telegram.model.button_text;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "messages")
public class MessagePayloadList {

    private List<MessagePayload> messagePayload;

    public List<MessagePayload> getMessagePayloads() {
        return messagePayload;
    }

    @XmlElement(name = "message")
    public void setMessagePayloads(List<MessagePayload> messagePayload) {
        this.messagePayload = messagePayload;
    }

}
