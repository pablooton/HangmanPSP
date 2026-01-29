package edu.pmoc.practicatrim.hangmanpsp.controller;

import edu.pmoc.practicatrim.hangmanpsp.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    public void onPlayButtonClick(ActionEvent actionEvent) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("game-view.fxml"));


            Scene scene = new Scene(fxmlLoader.load());


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


            stage.setTitle("Ahorcado - Partida");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("Error al cargar la vista del juego.");
        }
    }
}