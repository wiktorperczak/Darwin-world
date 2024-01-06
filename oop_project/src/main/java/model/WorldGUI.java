package model;

import javafx.application.Application;

public class WorldGUI {
    public static void main(String[] args) {
        OptionsManager.getInstance();
        Application.launch(SimulationApp.class, args);
    }
}
