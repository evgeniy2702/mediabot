package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.ButtonType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;
import ua.ukrposhta.mediabot.utils.type.MessageType;

import java.util.stream.Collectors;

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
            case LANGUAGE:
                text = MessageType.LANGUAGE.getText();
                break;
            case START:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.START.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;

            case MEDIA:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.MEDIA.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;

            case NAME_SURNAME:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.NAME_SURNAME.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;
            case PHONE:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.PHONE.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;
            case EMAIL:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.EMAIL.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;
            case SUBJECT:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.SUBJECT.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;
            case WE_CONTACT:
                text = context.getUser().getMessagesListBot().getMessages().stream()
                        .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.WE_CONTACT.name()))
                        .collect(Collectors.toList()).get(0).getTxt();
                break;
            default:
                if(context.getUser().getMessagesListBot() != null)
                    if(context.getInput().equalsIgnoreCase(ButtonType.START.name()))
                        text = context.getUser().getMessagesListBot().getMessages().stream()
                                .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.END.name()))
                                .collect(Collectors.toList()).get(0).getTxt();
                    else if(context.getInput().equalsIgnoreCase(ButtonType.END_WORK.getText()))
                        text = context.getUser().getMessagesListBot().getMessages().stream()
                                .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.END.name()))
                                .collect(Collectors.toList()).get(0).getTxt();
                    else if(context.getInput().equalsIgnoreCase(context.getUser().getButtonsNameList().getMessages().stream()
                            .filter(b -> b.getType().equalsIgnoreCase(ButtonType.END_WORK.name()))
                            .collect(Collectors.toList()).get(0).getTxt()))
                        text = context.getUser().getMessagesListBot().getMessages().stream()
                                .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.END.name()))
                                .collect(Collectors.toList()).get(0).getTxt();
                    else
                            text = context.getUser().getMessagesListBot().getMessages().stream()
                                .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.ERROR.name()))
                                .collect(Collectors.toList()).get(0).getTxt();
                else
                    text = MessageType.END.getText();
                break;
        }

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : state - " + state +
                "; text - " + text);

        SendMessage message = sendMessageBot.sendMessageBot(context,text,replyKeyboardMarkup, state);

        telegramLogger.info("handlerInlineKeyboard method HandlerInlineMessageType.class : message - " + message.toString());

    }
}
