package presenter;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import model.application.options.OptionsManager;
import model.map.RectangularMap;
import model.map.WorldMap;
import model.map.utils.Vector2d;
import model.simulation.Simulation;
import model.simulation.SimulationEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartPresenter {
    @FXML
    public ComboBox<Integer> simulationSpeed;
    private int id = 0;
    @FXML
    public ComboBox<Integer> numberOfAnimals;
    @FXML
    public ComboBox<Integer> height;
    @FXML
    public ComboBox<Integer> width;
    @FXML
    public ComboBox<Integer> animalLife;
    @FXML
    public CheckBox useReverseGenotype;
    @FXML
    public ComboBox<Integer> genotypeLength;
    @FXML
    public ComboBox<Integer> grassEnergy;
    @FXML
    public CheckBox useTunnels;
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
    public ComboBox<Integer> numberOfGrassPerDay;
    @FXML
    public ComboBox<Integer> startingGrassNumber;
    @FXML
    public ComboBox<String> useConfiguration;

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
            energyLossOnBreed.getItems().add(i);
            minimalEnergyToBreed.getItems().add(i);
            minGensToMutate.getItems().add(i);
            maxGensToMutate.getItems().add(i);
            numberOfGrassPerDay.getItems().add(i);
            startingGrassNumber.getItems().add(i);
        }

        for (int i = 1; i <= 10; i++){
            simulationSpeed.getItems().add(i);
        }

        simulationSpeed.setValue(5);
        startingGrassNumber.setValue(5);
        numberOfGrassPerDay.setValue(5);
        grassEnergy.setValue(2);
        animalLife.setValue(5);
        genotypeLength.setValue(5);
        minimalEnergyToBreed.setValue(5);
        maxGensToMutate.setValue(1);
        minGensToMutate.setValue(1);
        useTunnels.setSelected(true);
        useReverseGenotype.setSelected(true);

        updateTunnels();
        updateEnergyLossOnBreed();
        updateGensToMutate();

        updateConfigurations();
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
        maxGensToMutate.setValue(1);
        minGensToMutate.setValue(1);
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

    private int numberOfCOnfigurationsInFolder() {
        int idFileCount = 0;
        File folder = new File("konfiguracje");
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                    idFileCount++;
                }
            }
        }
        return idFileCount;
    }

    @FXML
    private void updateConfigurations() {
        int num = numberOfCOnfigurationsInFolder();
        for (int i = 0; i < num; i++) {
            useConfiguration.getItems().add("konfiguracja" + (i+1));
        }
        useConfiguration.setValue("konfiguracja" + 1);
    }

    public void useSetConfiguration() {
        String filePath = "konfiguracje/" + useConfiguration.getValue() + ".csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] columnTitles = reader.readLine().split(",");
            String[] values = reader.readLine().split(",");

            Map<String, String> dataMap = new HashMap<>();
            for (int i = 0; i < columnTitles.length; i++) {
                dataMap.put(columnTitles[i], values[i]);
            }

            height.setValue(Integer.parseInt(dataMap.get("Wysokość")));
            width.setValue(Integer.parseInt(dataMap.get("Szerokość")));
            numberOfAnimals.setValue(Integer.parseInt(dataMap.get("Liczba zwierząt")));
            useTunnels.setSelected(Boolean.parseBoolean(dataMap.get("Występowanie tuneli")));
            numberOfTunnels.setValue(Integer.parseInt(dataMap.get("Liczba tuneli")));
            animalLife.setValue(Integer.parseInt(dataMap.get("Początkowa energia zwierząt")));
            genotypeLength.setValue(Integer.parseInt(dataMap.get("Długość genotypu")));
            useReverseGenotype.setSelected(Boolean.parseBoolean(dataMap.get("Odtwarzanie genotypu od tyłu")));
            minimalEnergyToBreed.setValue(Integer.parseInt(dataMap.get("Min energii do rozmnażania")));
            energyLossOnBreed.setValue(Integer.parseInt(dataMap.get("Utrata energii przy rozmnażaniu")));
            maxGensToMutate.setValue(Integer.parseInt(dataMap.get("Max genów zmieniane w mutacji")));
            minGensToMutate.setValue(Integer.parseInt(dataMap.get("Min genów zmieniane w mutacji")));
            startingGrassNumber.setValue(Integer.parseInt(dataMap.get("Liczba traw na start")));
            grassEnergy.setValue(Integer.parseInt(dataMap.get("Wartość energetyczna trawy")));
            numberOfGrassPerDay.setValue(Integer.parseInt(dataMap.get("Liczba nowych traw na runde")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportConfiguration() {
        int id = 1 + numberOfCOnfigurationsInFolder();
        String filePath = "konfiguracje/konfiguracja" + id + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Wysokość,Szerokość,Liczba zwierząt,Występowanie tuneli,Liczba tuneli,Początkowa energia zwierząt," +
                    "Długość genotypu,Odtwarzanie genotypu od tyłu,Min energii do rozmnażania,Utrata energii przy rozmnażaniu," +
                    "Max genów zmieniane w mutacji,Min genów zmieniane w mutacji,Liczba traw na start," +
                    "Wartość energetyczna trawy,Liczba nowych traw na runde");
            writer.newLine();

            StringBuilder rowBuilder = new StringBuilder();
            rowBuilder.append(height.getValue()).append(",");
            rowBuilder.append(width.getValue()).append(",");
            rowBuilder.append(numberOfAnimals.getValue()).append(",");
            rowBuilder.append(useTunnels.isSelected()).append(",");
            rowBuilder.append(numberOfTunnels.getValue()).append(",");
            rowBuilder.append(animalLife.getValue()).append(",");
            rowBuilder.append(genotypeLength.getValue()).append(",");
            rowBuilder.append(useReverseGenotype.isSelected()).append(",");
            rowBuilder.append(minimalEnergyToBreed.getValue()).append(",");
            rowBuilder.append(energyLossOnBreed.getValue()).append(",");
            rowBuilder.append(maxGensToMutate.getValue()).append(",");
            rowBuilder.append(minGensToMutate.getValue()).append(",");
            rowBuilder.append(startingGrassNumber.getValue()).append(",");
            rowBuilder.append(grassEnergy.getValue()).append(",");
            rowBuilder.append(numberOfGrassPerDay.getValue()).append(",");
            writer.write(rowBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
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
        optionsManager.setUseTunnels(useReverseGenotype.isSelected());
        optionsManager.setGenotypeLength(genotypeLength.getValue());
        optionsManager.setGrassEnergy(grassEnergy.getValue());
        optionsManager.setNumberOfTunnels(numberOfTunnels.getValue());
        optionsManager.setUseTunnels(useTunnels.isSelected());
        optionsManager.setEnergyLossOnBreed(energyLossOnBreed.getValue());
        optionsManager.setMinimalEnergyToBreed(minimalEnergyToBreed.getValue());
        optionsManager.setMaxGensToMutate(maxGensToMutate.getValue());
        optionsManager.setMinGensToMutate(minGensToMutate.getValue());
        optionsManager.setUseReverseGenotype(useReverseGenotype.isSelected());
        optionsManager.setNumberOfGrassPerDay(numberOfGrassPerDay.getValue());
        optionsManager.setStartingGrassNumber(startingGrassNumber.getValue());
        optionsManager.setSimulationSpeed(simulationSpeed.getValue());

        List<Vector2d> positions = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < optionsManager.getNumberOfAnimals(); i++) {
            int num = random.nextInt((optionsManager.getWidth() + 1) * (optionsManager.getHeight() + 1) - 1);
            int x = num % (optionsManager.getWidth() + 1);
            int y = num / (optionsManager.getWidth() + 1);
            positions.add(new Vector2d(x, y));
        }


        WorldMap map = new RectangularMap(optionsManager.getWidth(), optionsManager.getHeight(), id, optionsManager);
        id += 1;

        Simulation simulation = new Simulation(positions, map);
        SimulationPresenter simulationPresenter = loader.getController();

        map.registerObserver(simulationPresenter);
        simulationPresenter.setSimulation(simulation);
        simulationPresenter.initializePresenter(optionsManager, map);


        stage.setOnCloseRequest(event -> {
                stopSimulation(simulation);
        });
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
    }

    public void showLegendWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("legend.fxml"));
        Stage stage = new Stage();
        VBox viewRoot = loader.load();
        var scene = new Scene(viewRoot);
        stage.setScene(scene);
        stage.setTitle("Legend");
        stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        stage.minHeightProperty().bind(viewRoot.minHeightProperty());

        stage.show();
    }

    private void stopSimulation(Simulation simulation){
        simulation.stopSimulation();
    }
}
