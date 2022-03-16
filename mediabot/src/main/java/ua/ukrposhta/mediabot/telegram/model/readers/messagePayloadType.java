package ua.ukrposhta.mediabot.telegram.model.readers;

public enum messagePayloadType {
    TEXT("text");

    private final String type;

    messagePayloadType(String type) {
        this.type = type;
    }

    public String getmessagePayloadType() {
        return type;
    }
}