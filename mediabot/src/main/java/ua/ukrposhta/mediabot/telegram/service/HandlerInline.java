package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;
import ua.ukrposhta.mediabot.utils.type.MessageType;

@Component
public class HandlerInline implements HandlerInlineKeyboard {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private SendMessageBot sendMessageBot;

    @Autowired
    public void setSendMessageBot(SendMessageBot sendMessageBot) {
        this.sendMessageBot = sendMessageBot;
    }


    @Override
    public void handlerInlineKeyboard(Update update,
                                      BotContext context,
                                      ReplyKeyboardMarkup replyKeyboardMarkup) throws Exception {


        consoleLogger.info("start handlerInlineKeyboard method in HandlerInline.class");

        TelegramBotState state = TelegramBotState.byId(context.getUser().getStateId());

        String text = update.getMessage().getText();

        switch (text){
            case "/start":
                text = MessageType.START.getText();
                break;
            case "Подати запит.":
                text = MessageType.MEDIA.getText();
                break;
            case "Розпочати новий запит.":
                text = MessageType.MEDIA.getText();
                break;
            default:
                text = MessageType.SUBJECT.getText();
                break;
        }

        telegramLogger.info("HandlerInline method handlerInlineKeyboard : state - " + state + "; text - " + text);

        SendMessage message = sendMessageBot.sendMessageBot(context,text,replyKeyboardMarkup, state);

        telegramLogger.info("HandlerInline method handlerInlineKeyboard : message - " + message.toString());

    }
}
