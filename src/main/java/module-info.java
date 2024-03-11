module com.example.examendi {
    requires javafx.controls;
    requires javafx.fxml;

    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires jasperreports;


    opens com.example.examendi to javafx.fxml;
    exports com.example.examendi;
    exports com.example.examendi.controllers;
    opens com.example.examendi.controllers;
    opens com.example.examendi.domain.cliente;
}