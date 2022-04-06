package ua.ukrposhta.mediabot.utils.type;

public enum LoggerType {
    CONSOLE("consoleLogger"),
    TELEGRAM("telegramLogger");

    private String text;

    LoggerType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
