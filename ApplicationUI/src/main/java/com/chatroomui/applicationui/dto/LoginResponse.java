package com.chatroomui.applicationui.dto;

public class LoginResponse {
    private String userToken;

    public LoginResponse(String token) {
        userToken = token;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
