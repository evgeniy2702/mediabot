package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;

@Component
public class SendMessageBot {


    public  SendMessage sendMessageBot(BotContext context, String text, ReplyKeyboardMarkup replyKeyboardMarkup, TelegramBotState state) throws TelegramApiException {

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();



        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);

        return message;
    }
}
