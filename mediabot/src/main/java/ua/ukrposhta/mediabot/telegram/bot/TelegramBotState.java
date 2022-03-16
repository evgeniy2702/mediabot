package ua.ukrposhta.mediabot.telegram.bot;

public enum  TelegramBotState {

    START("/start"),
    MEDIA("MEDIA"),
    NAME_SURNAME("NAME_SURNAME"),
    PHONE("PHONE"),
    EMAIL("EMAIL"),
    SUBJECT("SUBJECT"),
    WE_CONTACT("WE_CONTACT"),
    END("END"),
    PIAR_UNIT("PIAR_UNIT");

    private static TelegramBotState[] states;
    private final String name;

    TelegramBotState(){
        this.name = "/start";
    }


    TelegramBotState(String  name){
        this.name = name;
    }

    public static  TelegramBotState getInitialState(){
        return byId(0);
    }

    public static TelegramBotState byId(int id) {

        if(states == null){
            states = TelegramBotState.values();
        }
        return states[id];
    }
}
