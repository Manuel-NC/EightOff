module com.example.eightoff {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.eightoff to javafx.fxml;
    exports com.example.eightoff;
}