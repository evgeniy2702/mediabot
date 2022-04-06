package ua.ukrposhta.mediabot.utils.logger;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

public class ConsoleLogger extends BotLogger {

    private static volatile ConsoleLogger instance;
    private ch.qos.logback.classic.Logger logger = (Logger)LoggerFactory.getLogger(LoggerType.CONSOLE.getText());

    private ConsoleLogger() {}

    public static ConsoleLogger getInstance() {
        if (instance == null) {
            synchronized (ConsoleLogger.class) {
                if (instance == null) {
                    instance = new ConsoleLogger();
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
