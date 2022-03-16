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

@Component
public class HandleInlineTelegramBotState implements HandlerInlineKeyboard {

    private ReplyKeyBoard replyKeyBoard;
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    @Autowired
    public void setReplyKeyBoard(ReplyKeyBoard replyKeyBoard) {
        this.replyKeyBoard = replyKeyBoard;
    }

    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws Exception {

        consoleLogger.info("start handlerInlineKeyboard method in HandleInlineTelegramBotState.class");

        TelegramBotState state = TelegramBotState.byId(context.getUser().getStateId());
        String text = "";

        switch (state){
            case NAME_SURNAME:
                text = replyKeyBoard
                        .getMessagePayloadReader().getMessagePayload(TelegramBotState.NAME_SURNAME.name()).getCaption();
                break;
            case PHONE:
                text = replyKeyBoard
                        .getMessagePayloadReader().getMessagePayload(TelegramBotState.PHONE.name()).getCaption();
                break;
            case EMAIL:
                text = replyKeyBoard
                        .getMessagePayloadReader().getMessagePayload(TelegramBotState.EMAIL.name()).getCaption();
                break;
            case SUBJECT:
                text = replyKeyBoard
                        .getMessagePayloadReader().getMessagePayload(TelegramBotState.SUBJECT.name()).getCaption();
                break;
            case WE_CONTACT:
                text = replyKeyBoard
                        .getMessagePayloadReader().getMessagePayload(TelegramBotState.WE_CONTACT.name()).getCaption();
                break;
            default:
                text = replyKeyBoard
                        .getMessagePayloadReader().getMessagePayload(TelegramBotState.END.name()).getCaption();
                break;
        }

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : state - " + state +
                "; text - " + text);

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : message - " + message.toString());

        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);

    }
}
