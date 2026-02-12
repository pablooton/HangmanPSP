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
                        final String pts = ((String) data).split(":")[1];
                        Platform.runLater(() -> {
                            lblPuntos.setText(pts);
                            mostrarAlerta("Puntuación Global", "Tu puntuación acumulada es: " + pts);
                        });
                    }

                    else if (data instanceof EstadoPartida) {
                        EstadoPartida estado = (EstadoPartida) data;
                        this.miTurno = estado.isEsTuTurno();

                        Platform.runLater(() -> {
                            String progresoLimpio = estado.getProgreso().trim();
                            String textoActual = lblPalabra.getText().replace(" ", "");

                            if (!progresoLimpio.equals(textoActual) && progresoLimpio.contains("_")) {
                                if (progresoLimpio.chars().allMatch(c -> c == '_' || c == ' ')) {
                                    resetearBotonesTeclado();
                                    agregarLog("SISTEMA", "¡Nueva palabra cargada!");
                                }
                            }

                            lblPalabra.setText(estado.getProgreso().replace("", " ").trim());
                            lblVidas.setText(String.valueOf(estado.getVidas()));

                            if (estado.isJuegoTerminado()) {
                                panelLetras.setDisable(true);
                                agregarLog("SISTEMA", "Fin: " + estado.getMensaje());
                            } else {
                                panelLetras.setDisable(!estado.isEsTuTurno());

                                if (estado.isEsTuTurno()) {
                                    agregarLog("SISTEMA", "Es tu turno. Selecciona una letra.");
                                } else {
                                    agregarLog("SISTEMA", "Turno del oponente...");
                                }
                            }
                        });

                        if (estado.isJuegoTerminado()) break;
                    }
                }
            } catch (Exception e) {
                Platform.runLater(() -> agregarLog("SISTEMA", "Error de red: " + e.getMessage()));
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
    private void resetearBotonesTeclado() {
        panelLetras.getChildren().forEach(nodo -> {
            if (nodo instanceof Button) {
                nodo.setDisable(false);
            }
        });
    }
}