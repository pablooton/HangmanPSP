package edu.pmoc.practicatrim.hangmanpsp.controller;

import edu.pmoc.practicatrim.hangmanpsp.dao.UsuarioDao;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.network.client.ClientTCP;
import edu.pmoc.practicatrim.hangmanpsp.util.AppShell;
import edu.pmoc.practicatrim.hangmanpsp.util.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    public Button btnJugador1;
    public Button btnJugador2;
    private UsuarioDao usuarioDao;

    public void handleLogin(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String nombreUsuario = btn.getText();

        try {
            Jugador jugador = usuarioDao.cargarUser(nombreUsuario);

            ClientTCP cliente = new ClientTCP();
            cliente.conectar();

            cliente.enviarDatos(jugador);

            AppShell.getInstance().setCliente(cliente);
            AppShell.getInstance().setJugador(jugador);

            AppShell.getInstance().loadView(View.GAME);

        } catch (Exception e) {
            System.err.println("Error en el proceso de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.usuarioDao = new UsuarioDao();
    }
}