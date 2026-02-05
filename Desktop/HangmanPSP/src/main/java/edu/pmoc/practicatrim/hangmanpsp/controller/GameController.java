package edu.pmoc.practicatrim.hangmanpsp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;
import javafx.event.ActionEvent;

public class GameController {

    @FXML
    private Label lblPalabra;
    @FXML
    private TilePane panelLetras;
    @FXML
    private Label lblVidas;
    @FXML
    private TextArea  areaLog;

    private int vidasRestantes = 6;

    private String palabraSecreta = "HIBERNATE";
    private StringBuilder palabraVisible;

    @FXML
    public void initialize() {
        iniciarJuego();
    }

    private void iniciarJuego() {
        vidasRestantes = 6;
        lblVidas.setText(String.valueOf(vidasRestantes));

        palabraVisible = new StringBuilder();
        for (int i = 0; i < palabraSecreta.length(); i++) {
            palabraVisible.append("_");
        }
        actualizarLabelPalabra();
        agregarLog("SISTEMA", "¡Partida iniciada! Adivina la palabra.");
    }

    @FXML
    public void pulsarLetra(ActionEvent event) {
        Button boton = (Button) event.getSource();
        String letra = boton.getText();

        boton.setDisable(true);
        boolean acierto = palabraSecreta.contains(letra);

        if (acierto) {
            manejarAcierto(letra);
        } else {
            manejarFallo(letra);
        }
    }

    private void manejarAcierto(String letra) {
        char letraChar = letra.charAt(0);
        boolean ganada = true;

        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letraChar) {
                palabraVisible.setCharAt(i, letraChar);
            }
            if (palabraVisible.charAt(i) == '_') {
                ganada = false;
            }
        }

        actualizarLabelPalabra();
        agregarLog("JUGADOR", "¡Acierto! La letra " + letra + " es correcta.");

        if (ganada) {
            agregarLog("SISTEMA", "¡VICTORIA! Has adivinado la palabra.");
            panelLetras.setDisable(true);
        }
    }

    private void manejarFallo(String letra) {
        restarVida();
        agregarLog("JUGADOR", "Fallo. La letra " + letra + " no está.");
    }

    private void restarVida() {
        if (vidasRestantes > 0) {
            vidasRestantes--;
            lblVidas.setText(String.valueOf(vidasRestantes));

            if (vidasRestantes == 0) {
                agregarLog("SISTEMA", "GAME OVER. Te has quedado sin vidas.");
                agregarLog("SISTEMA", "La palabra era: " + palabraSecreta);
                panelLetras.setDisable(true);
            }
        }
    }

    private void actualizarLabelPalabra() {
        StringBuilder textoMostrar = new StringBuilder();
        for (int i = 0; i < palabraVisible.length(); i++) {
            textoMostrar.append(palabraVisible.charAt(i)).append(" ");
        }
        lblPalabra.setText(textoMostrar.toString().trim());
    }

    // Método para escribir en el "Chat"
    public void agregarLog(String emisor, String mensaje) {
        areaLog.appendText("[" + emisor + "]: " + mensaje + "\n");
    }
}