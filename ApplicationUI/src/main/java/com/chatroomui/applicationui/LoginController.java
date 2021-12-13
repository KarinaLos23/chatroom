package com.chatroomui.applicationui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    private String username;

    @FXML
    public void setNextScene(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("application.fxml"));
        Parent root = loader.load();

        setUsername();

        final ApplicationController controller = loader.getController();
        controller.initialize(username);

        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setOnCloseRequest(e -> {
            controller.stop();
        });
        stage.show();
    }

    private void setUsername() {
        this.username = usernameField.getText();
    }

    public String getUsername() {
        return this.username;
    }
}

