package ua.ukrposhta.mediabot.telegram.bot;

import lombok.Getter;
import lombok.Setter;
import ua.ukrposhta.mediabot.telegram.model.MessagesListBot;
import ua.ukrposhta.mediabot.telegram.model.User;

@Getter
@Setter
public class BotContext {

    private TelegramBot bot;
    private User user;
    private String input;

    public static BotContext of(TelegramBot bot,
                                User user,
                                String input){
        return new BotContext(bot, user, input);
    }

    public BotContext(TelegramBot bot,
                      User user,
                      String input) {
        this.bot = bot;
        this.user =user;
        this.input = input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}