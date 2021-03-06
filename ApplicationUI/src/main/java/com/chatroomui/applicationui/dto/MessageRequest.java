package com.chatroomui.applicationui.dto;

public class MessageRequest {
    private Long messageId;
    private Integer limit;
    private String userToken;
    private String channelName;

    public MessageRequest() {
    }

    public MessageRequest(Long messageId, Integer limit, String userToken, String channelName) {
        this.messageId = messageId;
        this.limit = limit;
        this.userToken = userToken;
        this.channelName = channelName;
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
