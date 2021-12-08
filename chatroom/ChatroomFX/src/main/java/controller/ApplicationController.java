package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ApplicationController {
    @FXML private TextField textField;
    @FXML private TextArea textArea;

    private String username;
    private String message;

    @FXML
    public void sendMessage() {
        setMessage();
        if (message.length() > 0 && message != null && !message.isEmpty()) {
            textArea.appendText("\n" + username + ": "+ message);
            textField.setText("");
        }
    }
    protected void userJoinedChat(String username) {
        textArea.appendText(username + " has joined the chat");
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    private void setMessage() {
        this.message = textField.getText();
    }

    protected String getMessage() {
        return message;
    }
}