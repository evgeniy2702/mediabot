package ua.ukrposhta.mediabot.telegram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private Long chatId;
    private Integer stateId;
    private String messageType;
    private String mediaName;
    private String name_surname;
    private String phone;
    private String email;
    private String subject;

    public User(Long chatId, Integer stateId) {
        this.chatId = chatId;
        this.stateId = stateId;
        this.email="";
        this.messageType = "";
        this.mediaName = "";
        this.name_surname = "";
        this.phone = "";
        this.subject = "";
    }
}
