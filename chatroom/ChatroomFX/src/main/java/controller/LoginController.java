package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LoginController {
    @FXML private TextField usernameField;
    private String username;

    @FXML
    public void setNextScene(ActionEvent event) throws IOException {
        URL url = new File ("C:\\Users\\Marijus\\Desktop\\Accenture\\chatroom-main\\ChatroomFX\\src\\main\\resources\\com\\bootcamp\\chatroom\\chatroomfx\\application.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        setUsername();

        ApplicationController controller = loader.getController();
        controller.setUsername(username);
        controller.userJoinedChat(username);

        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setUsername() {
        this.username = usernameField.getText();
    }

    public String getUsername() {
        return this.username;
    }
}
