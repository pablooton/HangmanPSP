package edu.pmoc.practicatrim.hangmanpsp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.event.ActionEvent;

public class GameController {

    @FXML
    private Label lblPalabra;

    @FXML
    private TilePane panelLetras;
    @FXML
    private Label lblVidas;

    private int vidasRestantes = 6;

    @FXML
    public void initialize() {
        vidasRestantes = 6;
        lblVidas.setText(String.valueOf(vidasRestantes));
    }

    @FXML
    public void pulsarLetra(ActionEvent event) {
        Button boton = (Button) event.getSource();
        String letra = boton.getText();

        boton.setDisable(true);
        boolean acierto = false;

        if (!acierto) {
            restarVida();
        }
    }

    private void restarVida() {
        if (vidasRestantes > 0) {
            vidasRestantes--;
            lblVidas.setText(String.valueOf(vidasRestantes));

            if (vidasRestantes == 0) {
                System.out.println("GAME OVER - Has perdido");
                panelLetras.setDisable(true);
            }
        }
    }
}