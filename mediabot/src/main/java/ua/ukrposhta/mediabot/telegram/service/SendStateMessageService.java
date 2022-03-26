package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.telegram.model.User;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.ButtonType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;
import ua.ukrposhta.mediabot.utils.type.MessageType;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SendStateMessageService {

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private ReplyKeyBoard replyKeyBoard;
    private HandlerInline handlerInline;
    private HandleInlineTelegramBotState handleInlineTelegramBotState;
    private HandlerInlineMessageType handlerInlineMessageType;

    @Autowired
    public void setReplyKeyBoard(ReplyKeyBoard replyKeyBoard) {
        this.replyKeyBoard = replyKeyBoard;
    }

    @Autowired
    public void setHandlerInlineMessageType(HandlerInlineMessageType handlerInlineMessageType){
        this.handlerInlineMessageType = handlerInlineMessageType;
    }

    @Autowired
    public void setHandleInlineTelegramBotState(HandleInlineTelegramBotState handleInlineTelegramBotState){
        this.handleInlineTelegramBotState = handleInlineTelegramBotState;
    }

    @Autowired
    public void setHandlerInline(HandlerInline handlerInline){
        this.handlerInline = handlerInline;
    }

    public TelegramBotState sendStateMessage(TelegramBotState state,
                                             BotContext context,
                                             Update update,
                                             User user) throws Exception {

        consoleLogger.info("start sendStateMessage method in SendStateMessageService.class");

        ReplyKeyboardMarkup replyKeyboardMarkup ;
        Pattern pattern;
        Matcher matcher;
        String text;

        switch (state){
            case START:
                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                handlerInline
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);
                state = TelegramBotState.MEDIA;

                break;

            case MEDIA:

                text = update.getMessage().getText();

                if(checkMatcheStandardMessage(text) ||
                    text.equalsIgnoreCase(ButtonType.REQUEST.getText()) ||
                    text.equalsIgnoreCase(ButtonType.REPEAT_REQUEST.getText())) {

                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);

                    context.getUser().setStateId(TelegramBotState.START.ordinal());
                    handleInlineTelegramBotState.handlerInlineKeyboard(update,context,replyKeyboardMarkup);

                    context.getUser().setStateId(TelegramBotState.MEDIA.ordinal());
                    handlerInline
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);
                    state = TelegramBotState.NAME_SURNAME;

                }else {
                    update.getMessage().setText(MessageType.ERROR.getText());
                    context.getUser().setStateId(TelegramBotState.START.ordinal());
                    user.setStateId(TelegramBotState.START.ordinal());
                    user.setMessageType(MessageType.ERROR.getText());
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handlerInlineMessageType
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }

                break;
            case NAME_SURNAME:

                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);
                state = TelegramBotState.PHONE;
                user.setMediaName(update.getMessage().getText());

                break;
            case PHONE:

                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);
                state = TelegramBotState.EMAIL;

                user.setName_surname(update.getMessage().getText());

                break;
            case EMAIL:

                text = update.getMessage().getText();
                pattern = Pattern.compile("^[0-9]{3} [0-9]{3} [0-9]{2} [0-9]{2}$");
                matcher = pattern.matcher(text);

                if(matcher.find()) {
                    state = TelegramBotState.SUBJECT;
                    String phoneFormater = "(" + text.split(" ")[0] + ") " + text.split(" ")[1] + " " + text.split(" ")[2] + " " + text.split(" ")[3];
                    user.setPhone(phoneFormater);
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }else {
                    state = TelegramBotState.EMAIL;
                    user.setStateId(state.ordinal());
                    user.setMessageType(MessageType.PHONE_ERROR.getText());
                    update.getMessage()
                            .setText(MessageType.PHONE_ERROR.getText());
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handlerInlineMessageType
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }

                break;
            case SUBJECT:

                text = update.getMessage().getText();
                pattern = Pattern.compile("^([A-Za-z0-9_-]+\\.)*[A-Za-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,3}$");
                matcher = pattern.matcher(text);

                if(matcher.find() ) {
                    user.setEmail(update.getMessage().getText());
                    state = TelegramBotState.WE_CONTACT;
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }else if(!context.getUser().getEmail().isEmpty()){
                    user.setSubject(update.getMessage().getText());
                    state = TelegramBotState.WE_CONTACT;
                    context.getUser().setStateId(state.ordinal());
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                    state = TelegramBotState.END;
                    user.setStateId(state.ordinal());
                    context.setUser(user);
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }else {
                    state = TelegramBotState.SUBJECT;
                    user.setStateId(state.ordinal());
                    user.setMessageType(MessageType.EMAIL_ERROR.getText());
                    update.getMessage()
                            .setText(MessageType.EMAIL_ERROR.getText());
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handlerInlineMessageType
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }

                break;
            case WE_CONTACT:

                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);
                state = TelegramBotState.END;
                user.setStateId(state.ordinal());
                user.setSubject(update.getMessage().getText());
                context.setUser(user);
                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                break;

            case END:

                if(update.getMessage().getText().equalsIgnoreCase("Закінчити роботу з ботом.")){
                    state = TelegramBotState.MEDIA;


                } else {
                    state = TelegramBotState.SUBJECT;
                }

                user.setStateId(state.ordinal());

                context.setUser(user);

                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                break;
        }

        telegramLogger.info("SendStateMessageService.class : state - " + state.name());

        return state;
    }

    private boolean checkMatcheStandardMessage(String text) {
        telegramLogger.info("SendStateMessageService.start checkMatcheStandardMessage method text is " + text);
        return Arrays.stream(MessageType.values()).anyMatch(message -> message.getText().equals(text));
    }
}
