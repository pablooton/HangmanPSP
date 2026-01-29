module edu.pmoc.practicatrim.hangmanpsp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens edu.pmoc.practicatrim.hangmanpsp to javafx.fxml;
    exports edu.pmoc.practicatrim.hangmanpsp;
    exports edu.pmoc.practicatrim.hangmanpsp.controller;
    opens edu.pmoc.practicatrim.hangmanpsp.controller to javafx.fxml;

}