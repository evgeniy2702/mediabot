package ua.ukrposhta.mediabot.utils.type;

public enum ButtonType {

    START("/start"),
    UA("Українська"),
    EN("English"),
    REQUEST("Подати запит."),
    REPEAT_REQUEST("Розпочати новий запит."),
    END_WORK("Закінчити роботу з ботом | Finish working with the bot.")
    ;

    private String text;

    ButtonType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
