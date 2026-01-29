package edu.pmoc.practicatrim.hangmanpsp.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameController {


    @FXML
    private Label lblPalabra;

    @FXML
    public void initialize() {
        lblPalabra.setText("_ _ _ A _ _");
    }

    @FXML
    public void pulsarLetra(ActionEvent event) {
        Button botonPulsado = (Button) event.getSource();
        String letra = botonPulsado.getText();

        System.out.println("Letra enviada: " + letra);

        botonPulsado.setDisable(true);

    }
}