module com.igirepay {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.igirepay.ui to javafx.fxml;
    opens com.igirepay.Lab1.model to javafx.base;

    exports com.igirepay;
    exports com.igirepay.ui;
    exports com.igirepay.console;
    exports com.igirepay.lab3.service;
    opens com.igirepay.lab3.service to javafx.fxml;
}
