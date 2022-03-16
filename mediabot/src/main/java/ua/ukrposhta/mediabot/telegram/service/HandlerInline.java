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

    private ReplyKeyBoard replyKeyBoard;
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    @Autowired
    public void setReplyKeyBoard(ReplyKeyBoard replyKeyBoard) {
        this.replyKeyBoard = replyKeyBoard;
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
                text= replyKeyBoard.getMessagePayloadReader().getMessagePayload(MessageType.GOOD_DAY.name()).getCaption();
                break;
            case "Подати запит.":
                text= replyKeyBoard.getMessagePayloadReader().getMessagePayload(TelegramBotState.MEDIA.name()).getCaption();
                break;
            case "Розпочати новий запит.":
                text= replyKeyBoard.getMessagePayloadReader().getMessagePayload(TelegramBotState.SUBJECT.name()).getCaption();
                break;
            default:
                text= replyKeyBoard.getMessagePayloadReader().getMessagePayload(TelegramBotState.SUBJECT.name()).getCaption();
                break;
        }

        telegramLogger.info("HandlerInline method handlerInlineKeyboard : state - " + state + "; text - " + text);

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(context.getUser().getChatId()))
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();

        telegramLogger.info("HandlerInline method handlerInlineKeyboard : message - " + message.toString());

        context.getUser().setStateId(state.ordinal());
        context.getBot().execute(message);

    }
}
