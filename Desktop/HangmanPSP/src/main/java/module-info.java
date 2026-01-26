module edu.pmoc.practicatrim.hangmanpsp {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.pmoc.practicatrim.hangmanpsp to javafx.fxml;
    exports edu.pmoc.practicatrim.hangmanpsp;

}