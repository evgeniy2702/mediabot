package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

@Component
public class SendMessageBot {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    public  SendMessage sendMessageBot(BotContext context, String text, ReplyKeyboardMarkup replyKeyboardMarkup, TelegramBotState state) throws TelegramApiException {

        consoleLogger.info("Start sendMessageBot method in SendMessageBot.class");

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();

        telegramLogger.info("SendMessageBot.class sendMessageBot method message = " + message.toString());

        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);

        return message;
    }
}
