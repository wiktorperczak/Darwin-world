package presenter;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.util.List;
import java.util.Map;

public class SimulationPresenter implements MapChangeListener {
    public Button stopFollowingButton;
    public Label daysSimulated;
    @FXML
    private GridPane mapGrid;
    boolean simulationPaused;
    Simulation simulation;
    int maxEnergy;
    private WorldMap map;

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

    @FXML
    private Label animalStatsLabel;
    @FXML
    private Label animalsGenotypeLabel;
    @FXML
    private Label genActivatedLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label grassEatenLabel;
    @FXML
    private Label numberOfKidsLabel;
    @FXML
    private Label numberOfDescendantLabel;
    @FXML
    private Label numberOfDaysLived;

    private int cellSize;
    private final int maxGridWidth = 600;
    private final int maxGridHeight = 600;

    private StatsHandler statsHandler;

    Animal currentFollowingAnimal = null;



    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }
    public void initializePresenter(OptionsManager optionsManager, WorldMap map){
        mapGrid.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        simulationPaused = false;
        maxEnergy = 2 * optionsManager.getAnimalLife();
        setAnimalsStatsVisibility(false);
        this.map = map;
        statsHandler = map.getStatsHandler();
        cellSize = calculateCellSize();
    }

    private int calculateCellSize(){
        Vector2d bounds = map.getBoundaries();
        int maxCellWidth = maxGridWidth / (bounds.getX() + 1);
        int maxCellHeight = maxGridHeight / (bounds.getY() + 1);
        return Math.max(maxCellWidth, maxCellHeight);
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            simulationPaused = !simulationPaused;
            if (simulationPaused) {
                simulation.pauseSimulation();
            } else {
                simulation.unPauseSimulation();
            }
            drawMap(map);
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message){
        Platform.runLater(() -> drawMap(worldMap));
    }

    private void drawMap(WorldMap map) {
        clearGrid();
        daysSimulated.setText("Day: " + map.getDaysSimulated().toString());
        Vector2d bounds = map.getBoundaries();

        for (int i = 0; i < bounds.getX() + 2; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }

        for (int i = 0; i < bounds.getY() + 2; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
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

        if (simulationPaused){
            for (int y = 0; y < bounds.getY(); y++){
                if (map.isEquatorPosition(y)){
                    for (int x = 0; x <= bounds.getX(); x++){
                        Rectangle rectangle = new Rectangle(cellSize, cellSize, Color.LIGHTGREEN);
                        GridPane.setHalignment(rectangle, HPos.CENTER);
                        mapGrid.add(rectangle, x + 1, bounds.getY() - y + 1);
                    }
                }
            }
        }

        for (Map.Entry<Vector2d, List<WorldElement>> entry : map.getAllElements().entrySet()) {
            WorldElement worldElement = entry.getValue().get(0);
            Node objectToDraw;

            if (worldElement.worldElementType == WorldElementType.ANIMAL){
                drawTunnel(entry, bounds);
                objectToDraw = createAnimal(worldElement, bounds);
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

            if (simulationPaused &&
                    worldElement.worldElementType == WorldElementType.ANIMAL &&
                    ((Animal) worldElement).genotypeHasGen(statsHandler.getMostPopularGen())){
                Circle circle = new Circle((double) cellSize /10, Color.BLACK);
                GridPane.setHalignment(circle, HPos.CENTER);
                circle.setMouseTransparent(true);
                mapGrid.add(circle, worldElement.getPosition().getX() + 1,
                        bounds.getY() - worldElement.getPosition().getY() + 1);
            }

        }

        if (currentFollowingAnimal != null){
            updateAnimalStats(currentFollowingAnimal);
        }
        updateStatsLabels(map);
    }

    private Node createAnimal(WorldElement worldElement, Vector2d bounds){
        double energyNormalized = (double) ((Animal) worldElement).getEnergy() / maxEnergy;
        energyNormalized = Math.min(energyNormalized, 1);
        Color animalColor = Color.rgb(255, (int) (255 * energyNormalized), 0);
        if (worldElement.equals(currentFollowingAnimal)){
            animalColor = Color.rgb(100, 0, (int) (255 * energyNormalized));
        }

        Rectangle invisibleRect = new Rectangle(cellSize, cellSize, Color.TRANSPARENT);
        Node objectToDraw = createTriangle((double) (cellSize * 2) /5, animalColor);
        invisibleRect.setMouseTransparent(false);
        objectToDraw.setMouseTransparent(true);
        GridPane.setHalignment(invisibleRect, HPos.CENTER);
        mapGrid.add(invisibleRect, worldElement.getPosition().getX() + 1,
                bounds.getY() - worldElement.getPosition().getY() + 1);
        objectToDraw.setRotate(45 * ((Animal) worldElement).getFacingDirection());
        invisibleRect.setOnMouseClicked(event -> displayAnimalInfo((Animal) worldElement));
        return objectToDraw;
    }

    private void displayAnimalInfo(Animal animal) {
        updateAnimalStats(animal);
        setAnimalsStatsVisibility(true);
        currentFollowingAnimal = animal;
        drawMap(map);
    }

    private Node createGrass(){
        return new Circle((double) cellSize /5, Color.GREEN);
    }

    private Node createTunnelEnter(){
        return new Circle((double) (cellSize * 2) /5, Color.DARKGRAY);
    }

    private Node createTunnelExit(){
        return new Rectangle((double) (cellSize * 4) /5, (double) (cellSize * 4) /5, Color.DARKGRAY);
    }

    private Polygon createTriangle(double size, Color color) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(0.0, 0.0, -size/2, 2*size, size/2, 2*size);
        triangle.setFill(color);
        GridPane.setHalignment(triangle, HPos.CENTER);
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

    private void clearGrid(){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void updateStatsLabels(WorldMap map) {
        statsHandler = map.getStatsHandler();
        numberOfAnimalsLabel.setText("Number of Animals: " + statsHandler.getNumberOfAnimals());
        numberOfGrassLabel.setText("Number of Grass: " + statsHandler.getNumberOfGrass());
        numberOfFreeSpacesLabel.setText("Number of Free Spaces: " + statsHandler.getNumberOfFreeSpaces());
        mostPopularGenLabel.setText("Most Popular Gen: " + statsHandler.getMostPopularGen());
        averageEnergyLabel.setText("Average Energy: " + statsHandler.getAverageEnergy());
        averageNumberOfKidsLabel.setText("Average Number of Kids: " + statsHandler.getAverageNumberOfKids());
        averageLengthOfDeadAnimalsLabel.setText("Average Length of Dead Animals: " + statsHandler.getAverageLengthOfDeadAnimals());
    }

    public void updateAnimalStats(Animal animal) {
        stopFollowingButton.setFocusTraversable(false);
        animalsGenotypeLabel.setText("Genotype: " + animal.getGenotype());
        genActivatedLabel.setText("Activated gen: " + animal.getActiveGen());
        energyLabel.setText("Energy: " + animal.getEnergy());
        grassEatenLabel.setText("Grass eaten: " + animal.getGrassEaten());
        numberOfKidsLabel.setText("Number of kids: " + animal.getNumberOfKids());
        animal.calculateNumberOfDescendants();
        numberOfDescendantLabel.setText("Number of descendants: " + animal.getNumberOfDescendants());
        if (animal.isAlive()) {
            numberOfDaysLived.setText("Days lived: " + animal.getNumberOfDaysLived());
        } else {
            numberOfDaysLived.setText("Day of death: " + animal.getDayOfDeath());
        }
    }

    public void setAnimalsStatsVisibility(boolean isVisible){
        animalStatsLabel.setVisible(isVisible);
        animalsGenotypeLabel.setVisible(isVisible);
        genActivatedLabel.setVisible(isVisible);
        energyLabel.setVisible(isVisible);
        grassEatenLabel.setVisible(isVisible);
        numberOfKidsLabel.setVisible(isVisible);
        numberOfDescendantLabel.setVisible(isVisible);
        numberOfDaysLived.setVisible(isVisible);
        stopFollowingButton.setVisible(isVisible);
    }

    @FXML
    private void stopFollowingAnimal() {
        currentFollowingAnimal = null;
        setAnimalsStatsVisibility(false);
        drawMap(map);
    }
}
