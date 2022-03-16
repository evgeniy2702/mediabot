package ua.ukrposhta.mediabot.utils.logger;

import org.apache.log4j.Logger;

public class ConsoleLogger extends BotLogger {

    private static volatile ConsoleLogger instance;
    private Logger logger = Logger.getLogger("consoleLogger");

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
