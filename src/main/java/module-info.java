module com.example.examendi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.examendi to javafx.fxml;
    exports com.example.examendi;
}