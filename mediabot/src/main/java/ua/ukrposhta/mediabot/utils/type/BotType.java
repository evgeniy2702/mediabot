package ua.ukrposhta.mediabot.utils.type;

public enum BotType {
    TELEGRAM("telegram");

    private String name;

    BotType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BotType getBotType(String type) {
        switch (type) {
            case "telegram":
                return TELEGRAM;
            default:
                return null;
        }
    }
}
