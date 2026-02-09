package edu.pmoc.practicatrim.hangmanpsp.controller;

import edu.pmoc.practicatrim.hangmanpsp.model.EstadoPartida;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.network.client.ClientTCP;
import edu.pmoc.practicatrim.hangmanpsp.util.AppShell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;

public class GameController {

    @FXML private Label lblPalabra;
    @FXML private TilePane panelLetras;
    @FXML private Label lblVidas;
    @FXML private Label lblPuntos;
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

                    if (data instanceof String && ((String) data).startsWith("PUNTUACION:")) {
                        String pts = ((String) data).split(":")[1];
                        Platform.runLater(() -> {
                            lblPuntos.setText(pts);
                            mostrarAlerta("Puntuación Global", "Tu puntuación total acumulada es: " + pts);
                        });
                    }
                    else if (data instanceof EstadoPartida) {
                        EstadoPartida estado = (EstadoPartida) data;

                        this.miTurno = estado.isEsTuTurno();

                        Platform.runLater(() -> {
                            String nuevoProgreso = estado.getProgreso().replace("", " ").trim();
                            String progresoAnterior = lblPalabra.getText().replace(" ", "");
                            String progresoNuevoSinEspacios = estado.getProgreso();

                            if (!progresoAnterior.isEmpty() &&
                                    !progresoAnterior.contains("_") &&
                                    progresoNuevoSinEspacios.contains("_")) {

                                panelLetras.getChildren().forEach(node -> node.setDisable(false));
                                agregarLog("SISTEMA", "¡Palabra acertada! Cargando siguiente ronda...");
                            }

                            lblPalabra.setText(nuevoProgreso);
                            lblVidas.setText(String.valueOf(estado.getVidas()));

                            if (estado.isJuegoTerminado()) {
                                panelLetras.setDisable(true);
                                agregarLog("SISTEMA", "Partida finalizada: " + estado.getMensaje());
                            } else {
                                panelLetras.setDisable(!estado.isEsTuTurno());


                                if (estado.isEsTuTurno()) {
                                    agregarLog("SISTEMA", "¡Es tu turno! Elige una letra.");
                                } else {
                                    agregarLog("SISTEMA", "Esperando jugada del oponente...");
                                }
                            }
                        });

                        if (estado.isJuegoTerminado()) break;
                    }
                }
            } catch (Exception e) {
                Platform.runLater(() -> agregarLog("SISTEMA", "Conexión perdida con el servidor."));
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void pulsarLetra(ActionEvent event) {
        if (!miTurno) return;

        Button boton = (Button) event.getSource();
        boton.setDisable(true);

        String letra = boton.getText();
        cliente.enviarDatos(letra.charAt(0));
    }

    @FXML
    public void pulsarCancelar() {
        cliente.enviarDatos("CANCELAR");
        AppShell.getInstance().loadView(edu.pmoc.practicatrim.hangmanpsp.util.View.LOGIN);
    }

    @FXML
    public void mostrarPuntuacion(ActionEvent event) {
        if (miTurno) {
            cliente.enviarDatos("PUNTUACION");
        } else {
            agregarLog("SISTEMA", "Espera a tu turno para consultar la puntuación.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void agregarLog(String emisor, String mensaje) {
        areaLog.appendText("[" + emisor + "]: " + mensaje + "\n");
    }
}