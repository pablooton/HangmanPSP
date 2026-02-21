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

    @ FXML public Button btnVolver;
    @FXML private Label lblPalabra;
    @FXML private TilePane panelLetras;
    @FXML private Label lblVidas;
    @FXML private Label lblPuntos;
    @FXML private TextArea areaLog;

    private ClientTCP cliente;
    private Jugador jugadorLogueado;
    private boolean miTurno = false;
    private java.util.Set<Character> misLetrasPulsadas = new java.util.HashSet<>();
    private int vidasAnteriores = -1;

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

                    if (data == null) {
                        break;
                    }

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
                            lblPalabra.setText(estado.getProgreso().replace("", " ").trim());
                            lblVidas.setText(String.valueOf(estado.getVidas()));
                            String nuevoMensaje = estado.getMensaje();
                            if (nuevoMensaje != null && !nuevoMensaje.isEmpty()) {
                                if (!areaLog.getText().endsWith(nuevoMensaje + "\n")) {
                                    agregarLog("SISTEMA", nuevoMensaje);
                                }
                            }
                            if (estado.getVidas() == 6 && vidasAnteriores != 6) {
                                misLetrasPulsadas.clear();
                            }
                            vidasAnteriores = estado.getVidas();

                            for (javafx.scene.Node nodo : panelLetras.getChildren()) {
                                if (nodo instanceof Button) {
                                    Button boton = (Button) nodo;
                                    char letraBoton = boton.getText().toUpperCase().charAt(0);

                                    if (estado.getLetrasAcertadas().contains(letraBoton)) {
                                        boton.setDisable(true);
                                    } else if (misLetrasPulsadas.contains(letraBoton)) {
                                        boton.setDisable(true);

                                    } else if (!estado.isJuegoTerminado()) {
                                        boton.setDisable(!estado.isEsTuTurno());
                                    }
                                }
                            }

                            if (estado.isJuegoTerminado()) {
                                panelLetras.setDisable(true);
                                btnVolver.setVisible(true);
                                btnVolver.setManaged(true);
                            } else {
                                panelLetras.setDisable(!estado.isEsTuTurno());
                            }
                        });

                        if (estado.isJuegoTerminado()) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Hilo de escucha cerrado.");
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
        misLetrasPulsadas.add(letra.charAt(0));
        cliente.enviarDatos(letra.charAt(0));
    }

    @FXML
    public void pulsarCancelar() {
        panelLetras.setDisable(true);
        cliente.enviarDatos("CANCELAR");
        Platform.runLater(() -> AppShell.getInstance().loadView(edu.pmoc.practicatrim.hangmanpsp.util.View.LOGIN));
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

    public void volverAlMenu(ActionEvent actionEvent) {
        if (cliente != null) {
            cliente.desconectar();
        }
        Platform.runLater(() -> AppShell.getInstance().loadView(edu.pmoc.practicatrim.hangmanpsp.util.View.LOGIN));
    }
}