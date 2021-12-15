module com.chatroomui.applicationui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires jakarta.ws.rs;

    opens com.chatroomui.applicationui;
    exports com.chatroomui.applicationui;
    exports com.chatroomui.applicationui.dto;
}