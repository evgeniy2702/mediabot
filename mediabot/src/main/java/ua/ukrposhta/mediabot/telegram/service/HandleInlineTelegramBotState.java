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
public class HandleInlineTelegramBotState implements HandlerInlineKeyboard {


    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private SendMessageBot sendMessageBot;

    @Autowired
    public void setSendMessageBot(SendMessageBot sendMessageBot) {
        this.sendMessageBot = sendMessageBot;
    }


    @Override
    public void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws Exception {

        consoleLogger.info("start handlerInlineKeyboard method in HandleInlineTelegramBotState.class");

        TelegramBotState state = TelegramBotState.byId(context.getUser().getStateId());
        String text = "";

        switch (state){
            case START:
                text = MessageType.GOOD_DAY.getText();
                break;
            case NAME_SURNAME:
                text = MessageType.NAME_SURNAME.getText();
                break;
            case PHONE:
                text = MessageType.PHONE.getText();
                break;
            case EMAIL:
                text = MessageType.EMAIL.getText();
                break;
            case SUBJECT:
                text = MessageType.SUBJECT.getText();
                break;
            case WE_CONTACT:
                text = MessageType.WE_CONTACT.getText();
                break;
            default:
                text = MessageType.END.getText();
                break;
        }

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : state - " + state +
                "; text - " + text);

        SendMessage message = sendMessageBot.sendMessageBot(context,text,replyKeyboardMarkup, state);

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : message - " + message.toString());

    }
}
