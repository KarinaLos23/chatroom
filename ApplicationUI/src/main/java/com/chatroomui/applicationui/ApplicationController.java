package com.chatroomui.applicationui;

import com.chatroomui.applicationui.dto.LoginRequest;
import com.chatroomui.applicationui.dto.LoginResponse;
import com.chatroomui.applicationui.dto.Message;
import com.chatroomui.applicationui.dto.MessageRequest;
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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationController {
    private final String hostname = "https://java-bootcamp-chatroom.herokuapp.com";
    @FXML private TextField textField;
    @FXML private TextArea textArea;
    Client client = ClientBuilder.newClient();

    private String username;
    private long lastMessageId;

    @FXML
    public void sendMessage() {
        String message = textField.getText();
        if (message.length() > 0) {
            textArea.appendText("\n" + username + ": "+ message);
            textField.setText("");
            WebTarget messagesTarget = client.target(hostname).path("message");
            Response response = messagesTarget
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(new Message(message, username), MediaType.APPLICATION_JSON));
            System.out.println("Response " + response.readEntity(String.class));
        }
    }
    private void userJoinedChat(String username) {
        textArea.appendText(username + " has joined the chat");
    }

    public void setUsername(String username) {
        this.username = username;
        userJoinedChat(username);

        // login example
//        WebTarget target = client.target("https://java-bootcamp-chatroom.herokuapp.com").path("login");
//        Response response = target
//                .request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(new LoginRequest(), MediaType.APPLICATION_JSON));
//        LoginResponse lr = response.readEntity(LoginResponse.class);
//        System.out.println("Response " + lr);


        //first query for messages
        WebTarget messagesTarget = client.target(hostname).path("messages");
        Response messagesResponse = messagesTarget
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(new MessageRequest(null, 3, "userToken"), MediaType.APPLICATION_JSON));
        Message[] msgs = messagesResponse.readEntity(Message[].class);
        for (Message m : msgs) {
            textArea.appendText("\n" + m.getUser() + ": "+ m.getMessage());
            lastMessageId = m.getId();
        }
        System.out.println("Response " + Arrays.toString(msgs));

        // continuous queries for messages
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                WebTarget messagesTarget = client.target(hostname).path("messages");
                Response messagesResponse = messagesTarget
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(new MessageRequest(lastMessageId, 0, "userToken"), MediaType.APPLICATION_JSON));
                Message[] msgs = messagesResponse.readEntity(Message[].class);
                for (Message m : msgs) {
                    if (!username.equals(m.getUser())) {
                        textArea.appendText("\n" + m.getUser() + ": "+ m.getMessage());
                    }
                    lastMessageId = m.getId();
                }
                System.out.println("Response " + Arrays.toString(msgs));
            }
        }, 1000, 2000);
    }
}
