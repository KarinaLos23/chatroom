package com.chatroomui.applicationui.dto;

public class MessageRequest {
    private Long messageId;
    private Integer limit;
    private String userToken;

    public MessageRequest() {
    }

    public MessageRequest(Long messageId, Integer limit, String userToken) {
        this.messageId = messageId;
        this.limit = limit;
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
