package ua.ukrposhta.mediabot.telegram.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.ukrposhta.mediabot.telegram.bot.BotContext;

import javax.xml.bind.JAXBException;

@Component
public interface HandlerInlineKeyboard {

    void handlerInlineKeyboard(Update update, BotContext context, ReplyKeyboardMarkup replyKeyboardMarkup) throws Exception;
}

