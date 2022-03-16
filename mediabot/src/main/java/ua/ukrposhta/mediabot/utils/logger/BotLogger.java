package ua.ukrposhta.mediabot.utils.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ua.ukrposhta.mediabot.utils.type.BotType;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

import java.io.IOException;
import java.util.Properties;

public abstract class BotLogger {

    public BotLogger() {
        configure();
    }

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

    public static BotLogger getLogger(BotType botType) {
        switch (botType) {
            case TELEGRAM:
                return TelegramLogger.getInstance();
            default:
                throw new IllegalArgumentException("Can't resolve bot type!");
        }
    }

    private void configure() {
        Properties logProperties = new Properties();
        try {
            logProperties.load(BotLogger.class.getClassLoader().getResourceAsStream("properties/log4j.properties"));
            PropertyConfigurator.configure(logProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info(Object message) {
        getLogger().info(message);
    }

    public void warn(Object message) {
        getLogger().warn(message);
    }

    public void error(Object message) {
        getLogger().error(message);
    }

    protected abstract Logger getLogger();
}
