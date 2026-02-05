package edu.pmoc.practicatrim.hangmanpsp.util;

public enum View {
    LOGIN("/fxml/login-view.fxml"),
    GAME("/fxml/game-view.fxml");

    private final String fxmlPath;


    View(String fxmlPath) {
        this.fxmlPath = fxmlPath;

    }

    public String getFxmlPath() {
        return fxmlPath;
    }

}
