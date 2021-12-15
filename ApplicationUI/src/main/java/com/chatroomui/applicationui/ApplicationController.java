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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;



public class ApplicationController {
    private final String hostname = "https://java-bootcamp-chatroom.herokuapp.com";
    private final Timer timer = new Timer();
    private final String mainChannel = "Main";
    @FXML
    private TabPane channelPane;
    @FXML
    private TextField textField;
    //@FXML
    //private TextArea messageArea;
    //@FXML
    //private TextFlow textFlow;
    Client client = ClientBuilder.newClient();

    private String username;
    private Color usernameColor;
    private long lastMessageId;
    private String userToken;

    private void addChannel(String name) {
        TextFlow flow = new TextFlow();
        flow.setStyle("-fx-font-size: 14");
        ScrollPane sp = new ScrollPane(flow);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);
        Tab tab = new Tab(name, sp);
        channelPane.getTabs().add(tab);
        channelPane.getSelectionModel().select(tab);
    }

    private String getCurrentChannel() {
        Tab tab = channelPane.getSelectionModel().getSelectedItem();
        return tab.getText();
    }

    private void addMessage (String channelName, String message, String user, ZonedDateTime time) {
        Optional<Tab> tab = channelPane.getTabs().stream().filter(t -> t.getText().equals(channelName)).findAny();
        if (tab.isEmpty()) {
            return;
        }
        TextFlow flow = (TextFlow) ((ScrollPane)tab.get().getContent()).getContent();
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
    }

//    public void textAreaFunctionality() {
//        messageArea.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                event.consume();
//                if (event.isShiftDown()) {
//                    messageArea.appendText(System.getProperty("line.separator"));
//                } else {
//                    if(!messageArea.getText().isEmpty()){
//                        sendMessage();
//                    }
//                }
//            }
//        });
//    }

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

    private void executeCommand(String message) {
        if (message.startsWith("/join ")) {
            String s = message.replaceFirst("^/join ", "").trim();
            addChannel(s);
        }
    }

    public void initialize(String username, Color color) {
        this.username = username;
        this.usernameColor = color;

        addChannel(mainChannel);

        // login example
        LoginResponse loginResponse = postRequest("login", new LoginRequest(username), LoginResponse.class);
        System.out.println("Response " + loginResponse);
        userToken = loginResponse.getUserToken();

        //first query for messages
        Message[] messages = postRequest("messages", new MessageRequest(null, 20, userToken,
                mainChannel), Message[].class);

        for (Message message : messages) {
            addMessage(message.getChannelName(), message.getMessage(), message.getSender(), message.getTimestamp());
            lastMessageId = message.getId();
        }
        System.out.println("Response " + Arrays.toString(messages));

        // continuous queries for messages
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Message[] messages = postRequest("messages", new MessageRequest(lastMessageId, 0, userToken, getCurrentChannel()),
                        Message[].class);
                Platform.runLater(() -> {
                    for (Message message : messages) {
                        if (!username.equals(message.getSender())) {
                            addMessage(message.getChannelName(), message.getMessage(), message.getSender(), message.getTimestamp());
                        }
                        lastMessageId = message.getId();
                    }
                });
                System.out.println("Response " + Arrays.toString(messages));
            }
        }, 1000, 2000);

        channelPane.getTabs().get(0).setClosable(false);
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