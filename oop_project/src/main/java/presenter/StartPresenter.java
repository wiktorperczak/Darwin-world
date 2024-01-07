package presenter;

import javafx.scene.control.ComboBox;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class StartPresenter {
    @FXML
    public ComboBox<Integer> numberOfAnimals;
    @FXML
    public ComboBox<Integer> height;
    @FXML
    public ComboBox<Integer> width;
    @FXML
    public ComboBox<Integer> animalLife;
    @FXML
    public ComboBox<Integer> genotypeLength;
    @FXML
    public ComboBox<Integer> grassEnergy;
    @FXML
    public ComboBox<Integer> numberOfTunnels;

    @FXML
    private void initialize() {
        for (int i = 1; i <= 15; i++) {
            width.getItems().add(i);
            height.getItems().add(i);
            numberOfAnimals.getItems().add(i);
            grassEnergy.getItems().add(i);
        }
        width.setValue(5);
        height.setValue(5);
        numberOfAnimals.setValue(3);
        grassEnergy.setValue(2);

        for (int i = 1; i <= 30; i++) {
            animalLife.getItems().add(i);
            genotypeLength.getItems().add(i);
        }
        animalLife.setValue(5);
        genotypeLength.setValue(5);

        updateTunnels();
    }

    @FXML
    private void updateTunnels() {
        numberOfTunnels.getItems().clear();
        for (int i = 0; i <= (width.getValue() + 1) * (height.getValue() + 1) / 2; i++) {
            numberOfTunnels.getItems().add(i);
        }
        numberOfTunnels.setValue(1);
    }

    private void configureStage(Stage primaryStage, VBox viewRoot){
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
    public void onSimulationStartClick() throws InterruptedException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        Stage stage = new Stage();
        VBox viewRoot = loader.load();
        configureStage(stage, viewRoot);

        stage.show();

        OptionsManager optionsManager = new OptionsManager();
        optionsManager.setNumberOfAnimals(numberOfAnimals.getValue());
        optionsManager.setWidth(width.getValue());
        optionsManager.setHeight(height.getValue());
        optionsManager.setAnimalLife(animalLife.getValue());
        optionsManager.setGenotypeLength(genotypeLength.getValue());
        optionsManager.setGrassEnergy(grassEnergy.getValue());
        optionsManager.setNumberOfTunnels(numberOfTunnels.getValue());
        optionsManager.setEnergyLossOnBreed(1);
        optionsManager.setMinimalEnergyToBreed(3);

        List<Vector2d> positions = List.of(new Vector2d(1,1), new Vector2d(2, 2));
//        List<Vector2d> positions = List.of();

        WorldMap map = new RectangularMap(optionsManager.getWidth(), optionsManager.getHeight(), optionsManager);

        Simulation simulation = new Simulation(positions, map);
        SimulationPresenter simulationPresenter = loader.getController();

        map.registerObserver(simulationPresenter);
        simulationPresenter.setSimulation(simulation);
        simulationPresenter.initializePresenter();


        stage.setOnCloseRequest(event -> {
                stopSimulation(simulation);
        });
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
    }

    private void stopSimulation(Simulation simulation){
        simulation.stopSimulation();
    }
}
