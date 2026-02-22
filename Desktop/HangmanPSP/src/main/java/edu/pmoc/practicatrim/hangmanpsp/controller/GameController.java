package edu.pmoc.practicatrim.hangmanpsp.controller;

import edu.pmoc.practicatrim.hangmanpsp.model.EstadoPartida;
import edu.pmoc.practicatrim.hangmanpsp.network.client.ClientTCP;
import edu.pmoc.practicatrim.hangmanpsp.util.AppShell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    @FXML public Button btnVolver;
    @FXML private Label lblPalabra;
    @FXML private TilePane panelLetras;
    @FXML private Label lblVidas;
    @FXML private Label lblPuntos;
    @FXML private TextArea areaLog;

    private ClientTCP cliente;
    private Set<Character> misLetrasPulsadas = new HashSet<>();
    private boolean miTurno = false;

    @FXML
    public void initialize() {
        this.cliente = AppShell.getInstance().getCliente();
        btnVolver.setVisible(false);
        btnVolver.setManaged(false);
        if (cliente != null) iniciarHiloEscucha();
    }

    private void iniciarHiloEscucha() {
        new Thread(() -> {
            try {
                while (true) {
                    Object data = cliente.recibirDatos();
                    if (data instanceof String && ((String) data).startsWith("PUNTUACION:")) {
                        String pts = ((String) data).split(":")[1];
                        Platform.runLater(() -> lblPuntos.setText(pts));
                    } else if (data instanceof EstadoPartida estado) {
                        Platform.runLater(() -> actualizarUI(estado));
                        if (estado.isJuegoTerminado()) break;
                    } else if (data == null) break;
                }
            } catch (Exception e) {}
        }).start();
    }

    private void actualizarUI(EstadoPartida estado) {
        if (estado.getLetrasAcertadas().isEmpty() && !misLetrasPulsadas.isEmpty()) {
            misLetrasPulsadas.clear();
        }

        lblPalabra.setText(estado.getProgreso().replace("", " ").trim());
        lblVidas.setText(String.valueOf(estado.getVidas()));
        if (estado.getMensaje() != null) areaLog.appendText("[SISTEMA]: " + estado.getMensaje() + "\n");

        if (estado.isJuegoTerminado()) {
            panelLetras.setDisable(true);
            btnVolver.setVisible(true);
            btnVolver.setManaged(true);
        } else {
            this.miTurno = estado.isEsTuTurno();
            panelLetras.setDisable(!miTurno);
            for (Node n : panelLetras.getChildren()) {
                if (n instanceof Button b) {
                    char l = b.getText().toUpperCase().charAt(0);
                    b.setDisable(estado.getLetrasAcertadas().contains(l) || misLetrasPulsadas.contains(l) || !miTurno);
                }
            }
        }
    }

    @FXML
    public void pulsarLetra(ActionEvent event) {
        if (!miTurno) return;
        Button b = (Button) event.getSource();
        char l = b.getText().toUpperCase().charAt(0);
        b.setDisable(true);
        misLetrasPulsadas.add(l);
        cliente.enviarDatos(l);
    }

    @FXML
    public void pulsarCancelar() {
        cliente.enviarDatos("CANCELAR");
    }

    @FXML
    public void mostrarPuntuacion(ActionEvent event) {
        if (cliente != null) {
            cliente.enviarDatos("PUNTUACION");
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        if (cliente != null) {
            cliente.desconectar();
        }
        Platform.runLater(() -> AppShell.getInstance().loadView(edu.pmoc.practicatrim.hangmanpsp.util.View.LOGIN));
    }
}