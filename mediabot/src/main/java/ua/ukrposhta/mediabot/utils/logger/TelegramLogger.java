package ua.ukrposhta.mediabot.utils.logger;

import org.apache.log4j.Logger;

public class TelegramLogger extends BotLogger{
    private static volatile TelegramLogger instance;
    private Logger logger = Logger.getLogger("telegramLogger");

    private TelegramLogger() {}

    public static TelegramLogger getInstance() {
        if (instance == null) {
            synchronized (TelegramLogger.class) {
                if (instance == null) {
                    instance = new TelegramLogger();
                }
            }
        }
        return instance;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}

