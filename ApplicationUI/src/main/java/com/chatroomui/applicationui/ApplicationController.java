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
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    private TextArea messageArea;
    @FXML
    private TextFlow textFlow;
    Client client = ClientBuilder.newClient();

    private String username;
    private long lastMessageId;
    private String userToken;

    public void textAreaFunctionality() {
        messageArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                if (event.isShiftDown()) {
                    messageArea.appendText(System.getProperty("line.separator"));
                } else {
                    if(!messageArea.getText().isEmpty()){
                        sendMessage();
                    }
                }
            }
        });
    }

    @FXML
    public void sendMessage() {
        String message = textField.getText();
        if (message.length() > 0) {
            Text nameText = new Text(username);
            //nameText.setFill(Color.RED); this was only for testing
            textFlow.getChildren().add(new Text(nameText.getText() + ": " + message + "\n"));
            textField.setText("");
            String response = postRequest("message", new Message(message, userToken, mainChannel), String.class);
            System.out.println("Response " + response);
        }
    }

    public void initialize(String username, Color color) {
        this.username = username;

        // login example
        LoginResponse loginResponse = postRequest("login", new LoginRequest(username), LoginResponse.class);
        System.out.println("Response " + loginResponse);
        userToken = loginResponse.getUserToken();

        //first query for messages
        Message[] messages = postRequest("messages", new MessageRequest(null, 20, userToken,
                mainChannel), Message[].class);

        for (Message message : messages) {
            Text nameText = new Text(message.getSender());
            nameText.setFill(color);
            textFlow.getChildren().add(new Text(nameText.getText() + ": " + message.getMessage() + "\n"));
            lastMessageId = message.getId();
        }
        System.out.println("Response " + Arrays.toString(messages));

        // continuous queries for messages
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Message[] messages = postRequest("messages", new MessageRequest(lastMessageId, 0, userToken, mainChannel),
                        Message[].class);
                for (Message message : messages) {
                    if (!username.equals(message.getSender())) {
                        Text nameText = new Text(message.getSender());
                        nameText.setFill(color);
                        textFlow.getChildren().add(new Text(nameText.getText() + ": " + message.getMessage() + "\n"));
                    }
                    lastMessageId = message.getId();
                }
                System.out.println("Response " + Arrays.toString(messages));
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