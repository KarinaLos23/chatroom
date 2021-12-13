package com.chatroomui.applicationui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setScene(new Scene(root, 250, 250));
        stage.setTitle("Chatroom application");
        stage.centerOnScreen();
        stage.show();
        Image appLogo = new Image("file:Chat-Room.png");
        stage.getIcons().add(appLogo);
    }

    public static void main(String[] args) {
        launch();
    }
}