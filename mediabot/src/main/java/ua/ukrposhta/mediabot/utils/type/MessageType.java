package ua.ukrposhta.mediabot.utils.type;

public enum  MessageType {
    GOOD_DAY("GOOD_DAY"),
    EMAIL_ERROR("EMAIL_ERROR"),
    PHONE_ERROR("PHONE_ERROR"),
    ERROR("ERROR"),
    PIAR_UNIT("PIAR_UNIT");

    private String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
