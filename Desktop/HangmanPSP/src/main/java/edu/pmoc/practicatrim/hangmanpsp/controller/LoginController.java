package edu.pmoc.practicatrim.hangmanpsp.controller;

import edu.pmoc.practicatrim.hangmanpsp.dao.UsuarioDao;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;


public class LoginController {

    public Button btnJugador1;
    public Button btnJugador2;
    private UsuarioDao usuarioDao;

    public void handleLogin(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String nombreUsuario = btn.getText();

        try {
            Jugador jugador = usuarioDao.cargarUser(nombreUsuario);

        } catch (Exception e) {
            System.err.println("Error en el proceso de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}