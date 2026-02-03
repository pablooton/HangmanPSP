module edu.pmoc.practicatrim.hangmanpsp {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    requires java.naming;
    requires java.sql;

    opens edu.pmoc.practicatrim.hangmanpsp.model to org.hibernate.orm.core, jakarta.persistence, com.google.gson;


    opens edu.pmoc.practicatrim.hangmanpsp.util to org.hibernate.orm.core;


    opens edu.pmoc.practicatrim.hangmanpsp.controller to javafx.fxml;
    opens edu.pmoc.practicatrim.hangmanpsp to javafx.fxml;


    exports edu.pmoc.practicatrim.hangmanpsp;
    exports edu.pmoc.practicatrim.hangmanpsp.controller;
    exports edu.pmoc.practicatrim.hangmanpsp.model;
}