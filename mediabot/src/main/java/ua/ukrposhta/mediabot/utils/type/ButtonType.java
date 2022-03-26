package ua.ukrposhta.mediabot.utils.type;

public enum ButtonType {

    REQUEST("Подати запит."),
    REPEAT_REQUEST("Розпочати новий запит."),
    END_WORK("Закінчити роботу з ботом.")
    ;

    private String text;

    ButtonType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
