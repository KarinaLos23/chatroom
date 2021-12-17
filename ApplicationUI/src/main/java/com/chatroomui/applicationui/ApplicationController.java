package com.chatroomui.applicationui;

import com.chatroomui.applicationui.dto.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ApplicationController {
    private final String hostname = "https://java-bootcamp-chatroom.herokuapp.com";
    private final String mainChannel = "Main";
    private final Timer timer = new Timer();
    @FXML
    private TabPane channelPane;
    @FXML
    private TextField textField;
    private Client client = ClientBuilder.newClient();

    private String username;
    private Color usernameColor;
    private String userToken;
    private Map<String, Long> lastMessageMap = new HashMap<>();

    public void initialize(String username, Color color) {
        this.username = username;
        this.usernameColor = color;

        // login
        LoginResponse loginResponse = postRequest("login", new LoginRequest(username), LoginResponse.class);
        System.out.println("Response " + loginResponse);
        userToken = loginResponse.getUserToken();

        addChannel(mainChannel);

        // continuous queries for messages
        scheduleMessageUpdates();

        channelPane.getTabs().get(0).setClosable(false);
    }

    @FXML
    public void sendMessage() {
        String message = textField.getText();
        if (message.length() > 0) {
            textField.setText("");
            if (message.charAt(0) == '/') {
                executeCommand(message);
            } else {
                addMessage(getCurrentChannel(), message, username, ZonedDateTime.now());
                String response = postRequest("message", new Message(message, userToken, getCurrentChannel()), String.class);
                System.out.println("Response " + response);
            }
        }
    }

    private void addChannel(String name) {
        TextFlow flow = new TextFlow();
        flow.setStyle("-fx-font-size: 14");
        ScrollPane sp = new ScrollPane(flow);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);
        Tab tab = new Tab(name, sp);
        channelPane.getTabs().add(tab);
        initialMessageQuery(name);
        channelPane.getSelectionModel().select(tab);
    }

    private String getCurrentChannel() {
        Tab tab = channelPane.getSelectionModel().getSelectedItem();
        return tab.getText();
    }

    private void addMessage(String channelName, String message, String user, ZonedDateTime time) {
        Optional<Tab> tab = channelPane.getTabs().stream().filter(t -> t.getText().equals(channelName)).findAny();
        if (tab.isEmpty()) {
            return;
        }
        ScrollPane scrollPane = (ScrollPane) tab.get().getContent();
        TextFlow flow = (TextFlow) scrollPane.getContent();
        Text nameText = new Text("  " + user + " > ");
        if (user.equals(username)) {
            nameText.setFill(usernameColor);
        } else {
            nameText.setFill(Color.BLUE);
        }

        Text timeText = new Text(time.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        timeText.setFill(Color.DARKGRAY);

        flow.getChildren().add(timeText);
        flow.getChildren().add(nameText);
        flow.getChildren().add(new Text(message));
        flow.getChildren().add(new Text(System.lineSeparator()));

        scrollPane.setVvalue(1);
    }

    private void executeCommand(String message) {
        if (message.startsWith("/join ")) {
            String s = message.replaceFirst("^/join ", "").trim();
            addChannel(s);
        }
    }

    private void scheduleMessageUpdates() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                final Long lastMessageId = lastMessageMap.get(getCurrentChannel());
                Message[] messages = postRequest("messages", new MessageRequest(lastMessageId, 100, userToken, getCurrentChannel()),
                        Message[].class);
                Platform.runLater(() -> {
                    for (Message message : messages) {
                        boolean ownMessage = username.equals(message.getSender());
                        if (!ownMessage) {
                            addMessage(message.getChannelName(), message.getMessage(), message.getSender(), message.getTimestamp());
                        }
                        lastMessageMap.put(message.getChannelName(), message.getId());
                    }
                });
                System.out.println("Response " + Arrays.toString(messages));
            }
        }, 0, 500);
    }

    private void initialMessageQuery(String channel) {
        Message[] messages = postRequest("messages", new MessageRequest(null, 20, userToken, channel),
                Message[].class);
        for (Message message : messages) {
            addMessage(message.getChannelName(), message.getMessage(), message.getSender(), message.getTimestamp());
            lastMessageMap.put(message.getChannelName(), message.getId());
        }
        System.out.println("Response " + Arrays.toString(messages));
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