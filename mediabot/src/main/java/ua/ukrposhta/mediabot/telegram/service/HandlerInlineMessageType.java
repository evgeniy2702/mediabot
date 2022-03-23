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
public class HandlerInlineMessageType implements HandlerInlineKeyboard {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private SendMessageBot sendMessageBot;

    @Autowired
    public void setSendMessageBot(SendMessageBot sendMessageBot) {
        this.sendMessageBot = sendMessageBot;
    }


    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws Exception {

        consoleLogger.info("start handlerInlineKeyboard method in HandlerInlineMessageType.class");

        TelegramBotState state = TelegramBotState.byId(context.getUser().getStateId());

        String messageType = context.getUser().getMessageType();

        switch (messageType){
            case "PHONE_ERROR":
                messageType = MessageType.PHONE_ERROR.getText();
                break;
            case "EMAIL_ERROR":
                messageType = MessageType.EMAIL_ERROR.getText();
                break;
            default:
                messageType = MessageType.ERROR.getText();
                break;
        }

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : messageType - " + messageType);

        SendMessage message = sendMessageBot.sendMessageBot(context, messageType ,replyKeyboardMarkup, state);

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : message - " + message.toString());


    }
}
