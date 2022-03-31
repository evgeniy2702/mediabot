package ua.ukrposhta.mediabot.utils.type;


public enum  MessageType {
    LANGUAGE("Оберіть мову  спілкування, будь ласка | Please select a language"),
    UA("Українська"),
    EN("English"),
    START("Доброго дня! Ми дуже раді вас бачити в нашому боті для ЗМІ !"),
    SELECT("Дякую за ваш вибір."),
    GOOD_DAY("Слава Україні !"),
    MEDIA("\uD83D\uDCF0 Напишіть назву медіа."),
    NAME_SURNAME("\uD83D\uDC64 Напишіть ваші ім'я та прізвище."),
    PHONE("\uD83D\uDD22 Напишіть номер телефону у форматі 067 244 44 45."),
    EMAIL("\uD83D\uDCE7 Напишіть email-адресу."),
    SUBJECT("❓ Напишіть тему запиту і перелік питань."),
    WE_CONTACT("✅ Готово! Ми зв'яжемося з вами сьогодні-завтра для уточнення деталей і термінів підготовки відповіді. Героям слава!"),
    END("На все добре. | Goodbye. "),
    PHONE_ERROR("\uD83D\uDD22 Ви вказали номер телефону не вірно. Напишіть будь ласка номер телефону у форматі 067 244 44 45 ще раз."),
    EMAIL_ERROR("\uD83D\uDCE7 Ви вказали свою email-адресу не вірно. Напишіть будь ласка email-адресу ще раз."),
    ERROR_CHOICE_LANGUAGE("Зробіть вибір з меню, будь ласка. | Make a choice from the menu, please."),
    ERROR("Упс! На жаль, не можу розпізнати Ваш запит. Будь ласка оберіть дію з меню ."),
    PIAR_UNIT("Нове звернення ЗМІ : ");

    private String text;

    MessageType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
