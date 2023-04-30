module com.example.pendufx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pendufx to javafx.fxml;
    exports com.example.pendufx;
}