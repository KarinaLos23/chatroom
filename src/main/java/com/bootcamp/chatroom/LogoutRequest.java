package com.bootcamp.chatroom;

public class LogoutRequest {
    private String userToken;

    public LogoutRequest() {
    }

    public LogoutRequest(String token) {
        userToken = token;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
