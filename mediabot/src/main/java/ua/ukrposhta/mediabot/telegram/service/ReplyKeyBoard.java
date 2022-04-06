package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.xml.sax.SAXException;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;
import ua.ukrposhta.mediabot.telegram.bot.TelegramBotState;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.ButtonType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@PropertySource({"classpath:properties/link_buttons_message.properties"})
public class ReplyKeyBoard {

    @Value("${path.xml.ukraine}")
    private String pathXmlUkraine ;
    @Value("${path.xml.ukraine.buttons}")
    private String pathXmlUkraineButtons;

    @Value("${path.xml.english}")
    private String pathXmlEnglish;
    @Value("${path.xml.english.buttons}")
    private String pathXmlEnglishButtons;

    private BotLogger consoleLogger = BotLogger.getLogger(LoggerType.CONSOLE);
    private BotLogger telegramLogger = BotLogger.getLogger(LoggerType.TELEGRAM);


    public ReplyKeyboardMarkup replyButtons(BotContext context) throws IOException, SAXException, ParserConfigurationException {

        consoleLogger.info("start replyButtons method in ReplyKeyBoard.class");

        ReplyKeyboardMarkup replyKeyboardMarkup;

        if(TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.PIAR_UNIT.name())) {

            replyKeyboardMarkup = ReplyKeyboardMarkup.builder().build();

        } else
            if(TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.END.name())){

                KeyboardRow keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(ButtonType.START.name()));

                List<KeyboardRow> rowList = new ArrayList<>();
                rowList.add(keyboardRow);

                replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                        .selective(true)
                        .resizeKeyboard(true)
                        .keyboard(rowList)
                        .build();
        } else
            if(TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.LANGUAGE.name())){

            KeyboardRow keyboardRowBottom = new KeyboardRow();
            keyboardRowBottom.add(new KeyboardButton(ButtonType.END_WORK.getText()));

            KeyboardRow keyboardRowTop = new KeyboardRow();
            keyboardRowTop.add(new KeyboardButton(ButtonType.UA.getText()));
            keyboardRowTop.add(new KeyboardButton(ButtonType.EN.getText()));


            List<KeyboardRow> rowList = new ArrayList<>();
            rowList.add(keyboardRowTop);
            rowList.add(keyboardRowBottom);

            replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                    .selective(true)
                    .resizeKeyboard(true)
                    .keyboard(rowList)
                    .build();

        }  else
            if(context.getInput().equalsIgnoreCase(ButtonType.END_WORK.getText()) ||
                   context.getInput().equalsIgnoreCase(ButtonType.UA.getText()) ||
                    context.getInput().equalsIgnoreCase(ButtonType.EN.getText()) ) {

                String buttonEND_WORK = context.getUser().getButtonsNameList().getMessages().stream()
                        .filter(button -> button.getType().equalsIgnoreCase(ButtonType.END_WORK.name()))
                        .collect(Collectors.toList()).get(0).getTxt();

                String buttonREQUEST = context.getUser().getButtonsNameList().getMessages().stream()
                        .filter(button -> button.getType().equalsIgnoreCase(ButtonType.REQUEST.name()))
                        .collect(Collectors.toList()).get(0).getTxt();

                KeyboardRow keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(buttonREQUEST));
                keyboardRow.add(new KeyboardButton(buttonEND_WORK));


                List<KeyboardRow> rowList = new ArrayList<>();
                rowList.add(keyboardRow);

                replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                        .selective(true)
                        .resizeKeyboard(true)
                        .keyboard(rowList)
                        .build();

        }else if(!TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.END.name()) &&
                !TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.LANGUAGE.name()) &&
                !TelegramBotState.byId(context.getUser().getStateId()).name().equalsIgnoreCase(TelegramBotState.PIAR_UNIT.name()) &&
                !context.getInput().equalsIgnoreCase(ButtonType.END_WORK.getText())) {

            String buttonEND_WORK = context.getUser().getButtonsNameList().getMessages().stream()
                    .filter(button -> button.getType().equalsIgnoreCase(ButtonType.END_WORK.name()))
                    .collect(Collectors.toList()).get(0).getTxt();

            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(buttonEND_WORK));

            replyKeyboardMarkup = getReplyKeyboardMarkup(keyboardRow);


        }  else {

                String buttonEND_WORK = context.getUser().getButtonsNameList().getMessages().stream()
                        .filter(button -> button.getType().equalsIgnoreCase(ButtonType.END_WORK.name()))
                        .collect(Collectors.toList()).get(0).getTxt();

                String buttonREPEAT_REQUEST = context.getUser().getButtonsNameList().getMessages().stream()
                        .filter(button -> button.getType().equalsIgnoreCase(ButtonType.REPEAT_REQUEST.name()))
                        .collect(Collectors.toList()).get(0).getTxt();

                KeyboardRow keyboardRow = new KeyboardRow();
                keyboardRow.add(new KeyboardButton(buttonREPEAT_REQUEST));
                keyboardRow.add(new KeyboardButton(buttonEND_WORK));

                replyKeyboardMarkup = getReplyKeyboardMarkup(keyboardRow);

            }

        telegramLogger.info("replyKeyboardMarkup : - " + replyKeyboardMarkup.toString());

        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(KeyboardRow keyboardRow){

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardRow);


        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .keyboard(rowList)
                .build();
    }
}
