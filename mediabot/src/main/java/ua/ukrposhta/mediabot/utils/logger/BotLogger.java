package ua.ukrposhta.mediabot.utils.logger;

import ch.qos.logback.classic.Logger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

public abstract class BotLogger {

    public static BotLogger getLogger(LoggerType type) {
        BotLogger logger = null;
        switch (type) {
            case TELEGRAM:
                logger = TelegramLogger.getInstance();
                break;
            case CONSOLE:
                logger = ConsoleLogger.getInstance();
                break;
            default:
                throw new IllegalArgumentException("Can't resolve logger type!");
        }
        return logger;
    }

    public void info(String  message) {
        getLogger().info(message);
    }

    public void error(String message) {
        getLogger().error(message);
    }

    protected abstract Logger getLogger();
}
