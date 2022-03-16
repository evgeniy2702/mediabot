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

    private ReplyKeyBoard replyKeyBoard;
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    @Autowired
    public void setReplyKeyBoard(ReplyKeyBoard replyKeyBoard) {
        this.replyKeyBoard = replyKeyBoard;
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws Exception {

        consoleLogger.info("start handlerInlineKeyboard method in HandlerInlineMessageType.class");

        TelegramBotState state = TelegramBotState.byId(context.getUser().getStateId());

        String messageType = context.getUser().getMessageType();

        switch (messageType){
            case "PHONE_ERROR":
                messageType= replyKeyBoard.getMessagePayloadReader().getMessagePayload(MessageType.PHONE_ERROR.getName()).getCaption();
                break;
            case "EMAIL_ERROR":
                messageType= replyKeyBoard.getMessagePayloadReader().getMessagePayload(MessageType.EMAIL_ERROR.getName()).getCaption();
                break;
            default:
                messageType= replyKeyBoard.getMessagePayloadReader().getMessagePayload(MessageType.ERROR.getName()).getCaption();
                break;
        }

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : messageType - " + messageType);

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(messageType)
                .replyMarkup(replyKeyboardMarkup)
                .build();

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : message - " + message.toString());

        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);

    }
}
