package ua.ukrposhta.mediabot.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.ukrposhta.mediabot.telegram.google.GoogleSheetsLive;
import ua.ukrposhta.mediabot.telegram.model.User;
import ua.ukrposhta.mediabot.telegram.service.SendStateMessageService;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.ButtonType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;
import ua.ukrposhta.mediabot.utils.type.MessageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:properties/bot.properties")
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegramBot.name}")
    private String botName;

    @Value("${telegramBot.token}")
    private String botToken;

    @Value("${telegram.piar.unit.chatId}")
    private String chatIdPiarUnit = "98280876";

    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);
    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);

    private Map<Long, User> users = new HashMap<>();

    private Integer numberOfCellExcelSheet = 0;

    private SendStateMessageService sendStateMessageService;

    private GoogleSheetsLive googleSheetsLive;

    @Autowired
    public void setSendStateMessageService(SendStateMessageService sendStateMessageService) {
        this.sendStateMessageService = sendStateMessageService;
    }

    @Autowired
    public void setGoogleSheetsLive(GoogleSheetsLive googleSheetsLive) {
        this.googleSheetsLive = googleSheetsLive;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        consoleLogger.info("start onUpdateReceived method in TelegramBot.class");

        telegramLogger.info("Update of onUpdateReceived method of telegram bot  : " + update.toString());

        if (update.hasMessage()) {

            if (update.getMessage().hasText()) {
                final String text = update.getMessage().getText();
                final long chatId = update.getMessage().getChatId();

                User user = users.get(chatId);

                TelegramBotState state;
                BotContext context;

                if (user == null) {
                    state = TelegramBotState.getInitialState();

                    if(update.getMessage().getText().equalsIgnoreCase(ButtonType.REQUEST.getText()) ||
                            update.getMessage().getText().equalsIgnoreCase(ButtonType.REPEAT_REQUEST.getText()))
                        state = TelegramBotState.MEDIA;

                    user = new User(chatId, state.ordinal());

                    context = BotContext.of(this,  user, text);


                } else {

                    if(text.equalsIgnoreCase(TelegramBotState.LANGUAGE.name()) ||
                        text.equalsIgnoreCase("START")){
                        state = TelegramBotState.LANGUAGE;
                    } else {
                        state = TelegramBotState.byId(user.getStateId());
                    }

                    user.setStateId(state.ordinal());

                    context = BotContext.of(this, user, text);

                }

                state = ifReplyKey(update, state, user);

                telegramLogger.info("state : " + state.name());
                telegramLogger.info("test : " + text);
                telegramLogger.info("context : " + context.toString());

                try {

                    if(user.getChatId() != Long.parseLong(chatIdPiarUnit)) {
                        state = sendStateMessageService.sendStateMessage(state, context, update, user);
                        user.setStateId(state.ordinal());
                        users.put(chatId, user);
                    }

                    telegramLogger.info("user : " + user.toString());

                    if(!user.getSubject().equalsIgnoreCase("") &&
                            (user.getStateId() == TelegramBotState.END.ordinal())) {
                        numberOfCellExcelSheet = googleSheetsLive.readNumberOfCellExcelSheetFromExcelSheet(context, numberOfCellExcelSheet);
                        sendNewRequestForPiar(context, chatIdPiarUnit);
                        users.remove(chatId);
                    }

                    telegramLogger.info("Name of cell in excel google sheet : " + numberOfCellExcelSheet);

                } catch (Exception e) {

                    telegramLogger.error("ERROR : " +Arrays.toString(e.getStackTrace()));

                }
            }
        }
    }

    private TelegramBotState ifReplyKey(Update update,
                                        TelegramBotState state,
                                        User user) {

        consoleLogger.info("start ifReplyKey method in TelegramBot.class");

        String text = update.getMessage().getText();

        if (update.getCallbackQuery() == null) {

            if (text.equalsIgnoreCase(ButtonType.END_WORK.getText())) {
                state = TelegramBotState.END;
                user.setStateId(state.ordinal());
            }
            if (user.getMessagesListBot() != null) {
                if (text.equalsIgnoreCase(user.getButtonsNameList().getMessages().stream()
                        .filter(b -> b.getType().equalsIgnoreCase(ButtonType.END_WORK.name()))
                        .collect(Collectors.toList()).get(0).getTxt())) {
                    state = TelegramBotState.END;
                    user.setStateId(state.ordinal());
                }
            }
        }
        return state;
    }

    private void sendNewRequestForPiar(BotContext context, String chatIdPiarUnit) {

        consoleLogger.info("start sendNewRequestForPiar method in TelegramBot.class");

        if(!context.getUser().getSubject().isEmpty()) {

            SendMessage message = SendMessage.builder()
                    .chatId(chatIdPiarUnit)
                    .text(MessageType.PIAR_UNIT.getText() + "\n1) Назва : " +
                            context.getUser().getMediaName() + ".\n2) Запит : " + context.getUser().getSubject() +
                            "\n3) Посилання на таблицю запитів : " +
                            "https://docs.google.com/spreadsheets/d/1juzFkS2cZctAT7exY4N9fS4WfwrWZ3AVqweKJXAy0hE/edit#gid=0")
                    .build();

            telegramLogger.info("In sendNewRequestForPiar method TelegramBot.class to : chatId = " + message.toString() +
                                ";  message = " + message.getText());

            try {

                context.getBot().execute(message);

            } catch (TelegramApiException e) {
                telegramLogger.error("ERROR in sendNewRequestForPiar method of TelegramBot.class : " + Arrays.asList(e.getStackTrace()));
            }
            telegramLogger.info("send message about request info to telegram of piar unit ukrposhta with chatId : " + chatIdPiarUnit);
        }
        telegramLogger.info("do not send message about request info to telegram of piar unit ukrposhta because subject of requestMedia is null.");
    }

}

