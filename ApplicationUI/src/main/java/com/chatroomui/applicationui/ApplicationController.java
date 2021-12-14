package com.chatroomui.applicationui;

import com.chatroomui.applicationui.dto.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationController {
    private final String hostname = "https://java-bootcamp-chatroom.herokuapp.com";
    private final Timer timer = new Timer();
    private final String mainChannel = "Main";
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    Client client = ClientBuilder.newClient();

    private String username;
    private long lastMessageId;
    private String userToken;

    @FXML
    public void sendMessage() {
        String message = textField.getText();
        if (message.length() > 0) {
            textArea.appendText(username + ": " + message + "\n");
            textField.setText("");
            String response = postRequest("message", new Message(message, userToken, mainChannel), String.class);
            System.out.println("Response " + response);
        }
    }

    public void initialize(String username) {
        this.username = username;

        // login example
        LoginResponse lr = postRequest("login", new LoginRequest(username), LoginResponse.class);
        System.out.println("Response " + lr);
        userToken = lr.getUserToken();

        //first query for messages
        Message[] msgs = postRequest("messages", new MessageRequest(null, 20, userToken,
                mainChannel), Message[].class);

        for (Message m : msgs) {
            textArea.appendText(m.getSender() + ": " + m.getMessage() + "\n");
            lastMessageId = m.getId();
        }
        System.out.println("Response " + Arrays.toString(msgs));

        // continuous queries for messages
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Message[] msgs = postRequest("messages", new MessageRequest(lastMessageId, 0, userToken, mainChannel),
                        Message[].class);
                for (Message m : msgs) {
                    if (!username.equals(m.getSender())) {
                        textArea.appendText(m.getSender() + ": " + m.getMessage() + "\n");
                    }
                    lastMessageId = m.getId();
                }
                System.out.println("Response " + Arrays.toString(msgs));
            }
        }, 1000, 2000);
    }

    public void stop() {
        timer.cancel();
        String response = postRequest("logout", new LogoutRequest(userToken), String.class);
        System.out.println("Response " + response);
    }

    private <T> T postRequest(String path, Object body, Class<T> responseType) {
        WebTarget target = client.target(hostname).path(path);
        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(body, MediaType.APPLICATION_JSON));
        return response.readEntity(responseType);
    }
}
