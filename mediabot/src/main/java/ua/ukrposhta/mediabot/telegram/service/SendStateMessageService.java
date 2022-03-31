package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.telegram.model.MessagesListBot;
import ua.ukrposhta.mediabot.telegram.model.User;
import ua.ukrposhta.mediabot.utils.handlerXml.MySaxHandler;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.parserXml.MySaxParser;
import ua.ukrposhta.mediabot.utils.type.ButtonType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;
import ua.ukrposhta.mediabot.utils.type.MessageType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class SendStateMessageService {

    @Value("${path.xml.ukraine}")
    private String pathXmlUkraine ;
    @Value("${path.xml.ukraine.buttons}")
    private String pathXmlUkraineButtons;

    @Value("${path.xml.english}")
    private String pathXmlEnglish;
    @Value("${path.xml.english.buttons}")
    private String pathXmlEnglishButtons;

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private ReplyKeyBoard replyKeyBoard;
    private HandlerInline handlerInline;
    private HandleInlineTelegramBotState handleInlineTelegramBotState;
    private HandlerInlineMessageType handlerInlineMessageType;
    private MySaxParser mySaxParser;
    private MySaxHandler mySaxHandler;

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

    @Autowired
    public void setMySaxParser(MySaxParser mySaxParser) {
        this.mySaxParser = mySaxParser;
    }

    @Autowired
    public void setMySaxHandler(MySaxHandler mySaxHandler) {
        this.mySaxHandler = mySaxHandler;
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

            case LANGUAGE:

                replyKeyboardMarkup =replyKeyBoard.replyButtons(context);

                context.getUser().setStateId(TelegramBotState.LANGUAGE.ordinal());

                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update,context,replyKeyboardMarkup);

                state = TelegramBotState.START;

                break;

            case START:

                text = update.getMessage().getText();

                if(text.equalsIgnoreCase(ButtonType.UA.getText()) || text.equalsIgnoreCase(ButtonType.EN.getText())) {

                    MessagesListBot messages = null;
                    MessagesListBot buttonsName = null;

                    if(text.equalsIgnoreCase(ButtonType.UA.getText())) {
                        context.getUser().setLanguage(ButtonType.UA.getText());
                        messages = mySaxParser.getObjectExchangeFromXML(pathXmlUkraine, mySaxHandler);
                        buttonsName = mySaxParser.getObjectExchangeFromXML(pathXmlUkraineButtons, mySaxHandler);
                    }
                    if(text.equalsIgnoreCase(ButtonType.EN.getText())) {
                        context.getUser().setLanguage(ButtonType.EN.getText());
                        messages = mySaxParser.getObjectExchangeFromXML(pathXmlEnglish, mySaxHandler);
                        buttonsName = mySaxParser.getObjectExchangeFromXML(pathXmlEnglishButtons, mySaxHandler);
                    }

                    context.getUser().setMessagesListBot(messages);
                    context.getUser().setButtonsNameList(buttonsName);

                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);

                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                    handlerInline
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                    state = TelegramBotState.MEDIA;

                } else {
                    update.getMessage().setText(MessageType.ERROR_CHOICE_LANGUAGE.getText());
                    context.getUser().setStateId(TelegramBotState.LANGUAGE.ordinal());
                    user.setMessageType(MessageType.ERROR_CHOICE_LANGUAGE.name());
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handlerInlineMessageType
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }
                break;

            case MEDIA:

                text = update.getMessage().getText();


                if(text.equalsIgnoreCase(context.getUser().getButtonsNameList().getMessages()
                    .stream().filter(msg -> msg.getType().equalsIgnoreCase(ButtonType.REQUEST.name()))
                    .collect(Collectors.toList()).get(0).getTxt()) ) {

                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);

                    context.getUser().setStateId(TelegramBotState.MEDIA.ordinal());

                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                    state = TelegramBotState.NAME_SURNAME;

                }else {

                    text =  context.getUser().getMessagesListBot().getMessages().stream()
                            .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.ERROR.name()))
                            .collect(Collectors.toList()).get(0).getTxt();

                    update.getMessage().setText(text);



                    context.setInput(ButtonType.END_WORK.getText());




                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);

                    handlerInlineMessageType
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                    user.setStateId(TelegramBotState.MEDIA.ordinal());
                    user.setMessageType(MessageType.ERROR.name());
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
                    user.setMessageType(MessageType.PHONE_ERROR.name());
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
                    user.setMessageType(MessageType.EMAIL_ERROR.name());
                    update.getMessage()
                            .setText(MessageType.EMAIL_ERROR.getText());
                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handlerInlineMessageType
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                }

                break;
            case WE_CONTACT:

                replyKeyboardMarkup = null;
                handleInlineTelegramBotState
                        .handlerInlineKeyboard(update, context, replyKeyboardMarkup);
                state = TelegramBotState.END;
                user.setStateId(state.ordinal());
                user.setSubject(update.getMessage().getText());
                context.setUser(user);
                replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                SendMessage message = SendMessage.builder()
                        .chatId(String.valueOf(user.getChatId()))
                        .text(user.getMessagesListBot().getMessages().stream()
                                .filter(msg -> msg.getType().equalsIgnoreCase(MessageType.END.name()))
                                .collect(Collectors.toList()).get(0).getTxt())
                        .replyMarkup(replyKeyboardMarkup)
                        .build();

                context.getBot().execute(message);

                break;

            case END:

                if(user.getButtonsNameList() != null ) {
                    String s = context.getUser().getButtonsNameList().getMessages().stream()
                            .filter(b -> b.getType().equalsIgnoreCase(ButtonType.END_WORK.name())).collect(Collectors.toList()).get(0)
                            .getTxt();
                    if (!update.getMessage().getText().equalsIgnoreCase(s)) {

                        state = TelegramBotState.MEDIA;

                    }

                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                } else {

                    replyKeyboardMarkup = replyKeyBoard.replyButtons(context);
                    handleInlineTelegramBotState
                            .handlerInlineKeyboard(update, context, replyKeyboardMarkup);

                    state = TelegramBotState.START;

                }
                user.setStateId(state.ordinal());
                context.setUser(user);
                break;
        }

        telegramLogger.info("SendStateMessageService.class : state - " + state.name());

        return state;
    }
}
