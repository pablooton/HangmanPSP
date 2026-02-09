package edu.pmoc.practicatrim.hangmanpsp.util;

import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.network.client.ClientTCP;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppShell {
    private static AppShell instance;
    private BorderPane mainLayout;
    private Stage primaryStage;
    private Map<View,Object> controllers = new HashMap<>();

    private ClientTCP cliente;
    private Jugador jugador;

    private AppShell(){}

    public static AppShell getInstance(){
        if (instance ==null){
            instance = new AppShell();
        }
        return instance;
    }

    public void init(Stage stage){
        this.mainLayout = new BorderPane();
        this.primaryStage = stage;
        Scene scene = new Scene(mainLayout, 900, 700);
        stage.setScene(scene);
    }

    public Object loadView(View view){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));
        try {
            Parent viewNode = fxmlLoader.load();
            primaryStage.getScene().setRoot(viewNode);

            Object controller = fxmlLoader.getController();
            controllers.put(view, controller);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la vista " + view, e);
        }
    }

    public ClientTCP getCliente() {
        return cliente;
    }

    public void setCliente(ClientTCP cliente) {
        this.cliente = cliente;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
}