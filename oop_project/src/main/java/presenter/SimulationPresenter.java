package presenter;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    private GridPane mapGrid;
    boolean simulationPaused;
    Simulation simulation;
    int maxEnergy;

    @FXML
    private Label numberOfAnimalsLabel;
    @FXML
    private Label numberOfGrassLabel;
    @FXML
    private Label numberOfFreeSpacesLabel;
    @FXML
    private Label mostPopularGenLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageNumberOfKidsLabel;
    @FXML
    private Label averageLengthOfDeadAnimalsLabel;



    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }
    public void initializePresenter(OptionsManager optionsManager){
        mapGrid.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        simulationPaused = false;
        maxEnergy = 2 * optionsManager.getAnimalLife();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            simulationPaused = !simulationPaused;
            if (simulationPaused) {
                simulation.pauseSimulation();
            } else {
                simulation.unPauseSimulation();
            }
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message){
        Platform.runLater(() -> {
            drawMap(worldMap, message);
        });
    }

    private void drawMap(WorldMap map, String message) {
        clearGrid();
        Vector2d bounds = map.getBoundaries();

        for (int i = 0; i < bounds.getX() + 2; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(50));
        }

        for (int i = 0; i < bounds.getY() + 2; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(50));
        }

        Label label;
        for (int i = 0; i < bounds.getX() + 1; i++){
            label = new Label(String.valueOf(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }
        for (int i = 0; i < bounds.getY() + 1; i++){
            label = new Label(String.valueOf(bounds.getY() - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }

        label = new Label("y\\x");
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.add(label, 0, 0);

        for (Map.Entry<Vector2d, List<WorldElement>> entry : map.getAllElements().entrySet()) {
            WorldElement worldElement = entry.getValue().get(0);
            Node objectToDraw;
            // Create an ImageView using the image path from the WorldElement
            int rotation = 0;
            if (worldElement.worldElementType == WorldElementType.ANIMAL){
                drawTunnel(entry, bounds);
                objectToDraw = createAnimal(worldElement);
            } else if (worldElement.worldElementType == WorldElementType.GRASS){
                drawTunnel(entry, bounds);
                objectToDraw = createGrass();
            } else if (worldElement.worldElementType == WorldElementType.TUNNELENTER){
                objectToDraw = createTunnelEnter();
            } else {
                objectToDraw = createTunnelExit();
            }
            GridPane.setHalignment(objectToDraw, HPos.CENTER);
            mapGrid.add(objectToDraw, entry.getKey().getX() + 1, bounds.getY() - entry.getKey().getY() + 1);

            updateStatsLabels(map);
//            ImageView imageView = createImageView(worldElement.getImagePath(), rotation);
//
//            // Set the alignment and add the ImageView to the GridPane
//            GridPane.setHalignment(imageView, HPos.CENTER);
//            mapGrid.add(imageView, entry.getKey().getX() + 1, bounds.getY() - entry.getKey().getY() + 1);
        }
    }

    private Node createAnimal(WorldElement worldElement){
        double energyNormalized = (double) ((Animal) worldElement).getEnergy() / maxEnergy;
        energyNormalized = Math.min(energyNormalized, 1);
        Color color = Color.rgb(255, (int) (255 * energyNormalized), 0); // Adjust color range as needed
        Node objectToDraw = createTriangle(20, color);
        objectToDraw.setRotate(45 * ((Animal) worldElement).getFacingDirection());
        return objectToDraw;
    }

    private Node createGrass(){
        return new Circle(10, Color.GREEN);
    }

    private Node createTunnelEnter(){
        return new Circle(20, Color.DARKGRAY);
    }

    private Node createTunnelExit(){
        return new Rectangle(40, 40, Color.DARKGRAY);
    }

    private Polygon createTriangle(double size, Color color) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(0.0, 0.0, -size/2, 2*size, size/2, 2*size);
        triangle.setFill(color);
        return triangle;
    }

    public void drawTunnel(Map.Entry<Vector2d, List<WorldElement>> entry, Vector2d bounds){
        List<WorldElement> tunnel= entry.getValue().stream()
                .filter(worldElement -> worldElement.worldElementType == WorldElementType.TUNNELENTER ||
                        worldElement.worldElementType == WorldElementType.TUNNELEXIT).toList();
        if (tunnel.isEmpty()) return;
        Node objectToDraw;
        if (tunnel.get(0).worldElementType == WorldElementType.TUNNELENTER){
            objectToDraw = createTunnelEnter();
        } else {
            objectToDraw = createTunnelExit();
        }
        GridPane.setHalignment(objectToDraw, HPos.CENTER);
        mapGrid.add(objectToDraw, entry.getKey().getX() + 1, bounds.getY() - entry.getKey().getY() + 1);
//        ImageView imageView = createImageView(tunnel.get(0).getImagePath(), 0);
//        GridPane.setHalignment(imageView, HPos.CENTER);
//        mapGrid.add(imageView, entry.getKey().getX() + 1, bounds.getY() - entry.getKey().getY() + 1);
    }

    private ImageView createImageView(String imagePath, int rotation) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        Rotate rotate = new Rotate(rotation, 25, 25);

        // Apply the transformation to the ImageView
        imageView.getTransforms().add(rotate);
        imageView.setFitWidth(50); // Set the width as needed
        imageView.setFitHeight(50); // Set the height as needed
        return imageView;
    }

    private void clearGrid(){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void updateStatsLabels(WorldMap map) {
        StatsHandler statsHandler = map.getStatsHandler();
        numberOfAnimalsLabel.setText("Number of Animals: " + statsHandler.getNumberOfAnimals());
        numberOfGrassLabel.setText("Number of Grass: " + statsHandler.getNumberOfGrass());
        numberOfFreeSpacesLabel.setText("Number of Free Spaces: " + statsHandler.getNumberOfFreeSpaces());
        mostPopularGenLabel.setText("Most Popular Gen: " + statsHandler.getMostPopularGen());
        averageEnergyLabel.setText("Average Energy: " + statsHandler.getAverageEnergy());
        averageNumberOfKidsLabel.setText("Average Number of Kids: " + statsHandler.getAverageNumberOfKids());
        averageLengthOfDeadAnimalsLabel.setText("Average Length of Dead Animals: " + statsHandler.getAverageLengthOfDeadAnimals());
    }
}
