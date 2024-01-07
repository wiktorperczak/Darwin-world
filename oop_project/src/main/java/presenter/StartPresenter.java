package presenter;

import com.sun.security.jgss.InquireType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public ComboBox<Integer> energyLossOnBreed;
    @FXML
    public ComboBox<Integer> minimalEnergyToBreed;
    @FXML
    public ComboBox<Integer> maxGensToMutate;
    @FXML
    public ComboBox<Integer> minGensToMutate;

    @FXML
    private void initialize() {
        for (int i = 1; i <= 50; i++) {
            width.getItems().add(i);
            height.getItems().add(i);
        }
        width.setValue(5);
        height.setValue(5);

        for (int i = 1; i <= 1000; i++) { numberOfAnimals.getItems().add(i); }
        numberOfAnimals.setValue(3);

        for (int i = 1; i <= 30; i++) {
            grassEnergy.getItems().add(i);
            animalLife.getItems().add(i);
            genotypeLength.getItems().add(i);
            // todo: energyloss < minimalenergy
            energyLossOnBreed.getItems().add(i);
            minimalEnergyToBreed.getItems().add(i);
            minGensToMutate.getItems().add(i);
            maxGensToMutate.getItems().add(i);
        }
        grassEnergy.setValue(2);
        animalLife.setValue(5);
        genotypeLength.setValue(5);
        minimalEnergyToBreed.setValue(5);
        maxGensToMutate.setValue(0);
        minGensToMutate.setValue(0);

        updateTunnels();
        updateEnergyLossOnBreed();
        updateGensToMutate();
    }

    @FXML
    private void updateTunnels() {
        numberOfTunnels.getItems().clear();
        for (int i = 0; i <= (width.getValue() + 1) * (height.getValue() + 1) / 2; i++) {
            numberOfTunnels.getItems().add(i);
        }
        numberOfTunnels.setValue(1);
    }

    @FXML
    private void updateEnergyLossOnBreed() {
        energyLossOnBreed.getItems().clear();
        for (int i = 0; i < minimalEnergyToBreed.getValue(); i++)
            energyLossOnBreed.getItems().add(i);
        energyLossOnBreed.setValue(1);
    }

    @FXML
    private void updateGensToMutate(){
        maxGensToMutate.getItems().clear();
        minGensToMutate.getItems().clear();
        for (int i = 0; i <= genotypeLength.getValue(); i++){
            maxGensToMutate.getItems().add(i);
            minGensToMutate.getItems().add(i);
        }
        maxGensToMutate.setValue(0);
        minGensToMutate.setValue(0);
    }

    @FXML
    private void updateMaxGensToMutate(){
        if (minGensToMutate.getValue() == null || maxGensToMutate.getValue() == null) return;
        if (minGensToMutate.getValue() > maxGensToMutate.getValue()){
            maxGensToMutate.setValue(minGensToMutate.getValue());
        }
    }

    @FXML
    private void updateMinGensToMutate(){
        if (minGensToMutate.getValue() == null || maxGensToMutate.getValue() == null) return;
        if (minGensToMutate.getValue() > maxGensToMutate.getValue()){
            minGensToMutate.setValue(maxGensToMutate.getValue());
        }
    }


    private void configureStage(Stage primaryStage, HBox viewRoot){
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
        HBox viewRoot = loader.load();
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
        optionsManager.setEnergyLossOnBreed(energyLossOnBreed.getValue());
        optionsManager.setMinimalEnergyToBreed(minimalEnergyToBreed.getValue());
        optionsManager.setMaxGensToMutate(maxGensToMutate.getValue());
        optionsManager.setMinGensToMutate(minGensToMutate.getValue());

//        List<Vector2d> positions = List.of(new Vector2d(1,1), new Vector2d(2, 2));
        List<Vector2d> positions = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < optionsManager.getNumberOfAnimals(); i++) {
            int num = random.nextInt((optionsManager.getWidth() + 1) * (optionsManager.getHeight() + 1) - 1);
            int x = num % (optionsManager.getWidth() + 1);
            int y = num / (optionsManager.getWidth() + 1);
            positions.add(new Vector2d(x, y));
        }


        WorldMap map = new RectangularMap(optionsManager.getWidth(), optionsManager.getHeight(), optionsManager);

        Simulation simulation = new Simulation(positions, map);
        SimulationPresenter simulationPresenter = loader.getController();

        map.registerObserver(simulationPresenter);
        simulationPresenter.setSimulation(simulation);
        simulationPresenter.initializePresenter(optionsManager);


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
