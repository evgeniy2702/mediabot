package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.ButtonType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyBoard {

    @Value("${telegram.piar.unit.chatId}")
    private String chatIdPiarUnit = "98280876";

    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);

    public ReplyKeyboardMarkup replyButtons(BotContext context) {

        consoleLogger.info("start replyButtons method in ReplyKeyBoard.class");

        ReplyKeyboardMarkup replyKeyboardMarkup;

        if(TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.PIAR_UNIT.name())) {

            replyKeyboardMarkup = ReplyKeyboardMarkup.builder().build();

        } else if(TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.START.name()) ||
                context.getInput().equalsIgnoreCase(ButtonType.END_WORK.getText())
        ) {

            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(ButtonType.REQUEST.getText()));

            List<KeyboardRow> rowList = new ArrayList<>();
            rowList.add(keyboardRow);

            replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                    .selective(true)
                    .resizeKeyboard(true)
                    .keyboard(rowList)
                    .build();

        } else if(!TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.END.name()) &&
                !TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.START.name()) &&
                !TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.PIAR_UNIT.name()) &&
                !context.getInput().equalsIgnoreCase(ButtonType.END_WORK.getText())) {


            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(ButtonType.END_WORK.getText()));

            List<KeyboardRow> rowList = new ArrayList<>();
            rowList.add(keyboardRow);

            replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                    .selective(true)
                    .resizeKeyboard(true)
                    .keyboard(rowList)
                    .build();

        }  else {
                KeyboardRow keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(ButtonType.END_WORK.getText()));
                keyboardRow.add(new KeyboardButton(ButtonType.REPEAT_REQUEST.getText()));

                List<KeyboardRow> rowList = new ArrayList<>();
                rowList.add(keyboardRow);

                replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                        .selective(true)
                        .resizeKeyboard(true)
                        .keyboard(rowList)
                        .build();

            }

        telegramLogger.info("replyKeyboardMarkup : - " + replyKeyboardMarkup.toString());

        return replyKeyboardMarkup;
    }

}
