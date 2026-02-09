package edu.pmoc.practicatrim.hangmanpsp.controller;

import edu.pmoc.practicatrim.hangmanpsp.model.EstadoPartida;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.network.client.ClientTCP;
import edu.pmoc.practicatrim.hangmanpsp.util.AppShell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;

public class GameController {

    @FXML private Label lblPalabra;
    @FXML private TilePane panelLetras;
    @FXML private Label lblVidas;
    @FXML private TextArea areaLog;

    private ClientTCP cliente;
    private Jugador jugadorLogueado;
    private boolean miTurno = false;

    @FXML
    public void initialize() {
        this.cliente = AppShell.getInstance().getCliente();
        this.jugadorLogueado = AppShell.getInstance().getJugador();

        if (cliente != null) {
            iniciarHiloEscucha();
        } else {
            agregarLog("SISTEMA", "Error: No hay conexión con el servidor.");
        }
    }

    private void iniciarHiloEscucha() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Object data = cliente.recibirDatos();

                    if (data == null) break;

                    if (data instanceof EstadoPartida) {
                        EstadoPartida estado = (EstadoPartida) data;

                        this.miTurno = estado.isEsTuTurno();

                        Platform.runLater(() -> {
                            lblPalabra.setText(estado.getProgreso().replace("", " ").trim());
                            lblVidas.setText(String.valueOf(estado.getVidas()));
                            panelLetras.setDisable(!estado.isEsTuTurno());

                            if (estado.isJuegoTerminado()) {
                                agregarLog("SISTEMA", "Partida finalizada: " + estado.getMensaje());
                                panelLetras.setDisable(true);
                            } else {
                                if (estado.isEsTuTurno()) {
                                    agregarLog("SISTEMA", "Es tu turno. Elige una letra.");
                                } else {
                                    agregarLog("SISTEMA", "Turno del oponente...");
                                }
                            }
                        });

                        if (estado.isJuegoTerminado()) break;
                    }
                }
            } catch (Exception e) {
                Platform.runLater(() -> agregarLog("SISTEMA", "Conexión perdida con el servidor."));
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void pulsarLetra(ActionEvent event) {
        if (!miTurno) return;

        Button boton = (Button) event.getSource();
        String letra = boton.getText();
        boton.setDisable(true);

        cliente.enviarDatos(letra.charAt(0));
    }
    @FXML
    public void pulsarCancelar() {
        cliente.enviarDatos("CANCELAR");
        AppShell.getInstance().loadView(edu.pmoc.practicatrim.hangmanpsp.util.View.LOGIN);
    }
    public void agregarLog(String emisor, String mensaje) {
        areaLog.appendText("[" + emisor + "]: " + mensaje + "\n");
    }
}