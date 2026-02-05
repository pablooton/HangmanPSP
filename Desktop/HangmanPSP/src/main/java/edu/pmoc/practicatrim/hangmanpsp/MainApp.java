package edu.pmoc.practicatrim.hangmanpsp;

import edu.pmoc.practicatrim.hangmanpsp.util.AppShell;
import edu.pmoc.practicatrim.hangmanpsp.util.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppShell.getInstance().init(stage);
        AppShell.getInstance().loadView(View.LOGIN);

        stage.setTitle("HANGMAN");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}