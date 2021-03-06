package com.duffin22.mdmessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matthewtduffin on 14/08/16.
 */
public class Message implements Comparable<Message> {

    public static final String BODY = "body";
    public static final String USER_ID = "userId";
    public static final String MESSAGE_ID = "messageId";
    public static final String DATE = "date";

    private String body;
    private String date;
    private String userId;
    private String messageId;

    public Message(String body, String date, String userId, String messageId) {
        this.body = body;
        this.date = date;
        this.userId = userId;
        this.messageId = messageId;

    }

    @Override
    public int compareTo(Message message) {

        return this.getMessageId().compareTo(message.getMessageId());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
