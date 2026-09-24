module com.mycompany.pruebafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.pruebafx to javafx.fxml;
    exports com.mycompany.pruebafx;
}
