package presenter;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private WorldMap map;
    @FXML
    private GridPane mapGrid;

    @FXML
    public Label argsText;
    @FXML
    private Label infoMove;

    boolean simulationPaused;
    Simulation simulation;

    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }
    public void initializePresenter(){
        mapGrid.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        simulationPaused = false;
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
        infoMove.setText(message);

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

//        for (Map.Entry<Vector2d, List<WorldElement>> entry : map.getAllElements().entrySet()) {
//            label = new Label(entry.getValue().get(0).toString());
//            GridPane.setHalignment(label, HPos.CENTER);
//            mapGrid.add(label, entry.getKey().getX() + 1,
//                    bounds.getY() - (entry.getKey().getY()) + 1);
//        }
        for (Map.Entry<Vector2d, List<WorldElement>> entry : map.getAllElements().entrySet()) {
            WorldElement worldElement = entry.getValue().get(0);

            // Create an ImageView using the image path from the WorldElement
            int rotation = 0;
            if (worldElement.worldElementType == WorldElementType.ANIMAL){
                drawTunnelUnderWorldElement(entry, bounds);
                rotation = 45 * ((Animal) worldElement).getFacingDirection();
            }
            if (worldElement.worldElementType == WorldElementType.GRASS){
                drawTunnelUnderWorldElement(entry, bounds);
            }
            ImageView imageView = createImageView(worldElement.getImagePath(), rotation);

            // Set the alignment and add the ImageView to the GridPane
            GridPane.setHalignment(imageView, HPos.CENTER);
            mapGrid.add(imageView, entry.getKey().getX() + 1, bounds.getY() - entry.getKey().getY() + 1);
        }
    }

    public void drawTunnelUnderWorldElement(Map.Entry<Vector2d, List<WorldElement>> entry, Vector2d bounds){
        List<WorldElement> tunnel= entry.getValue().stream()
                .filter(worldElement -> worldElement.worldElementType == WorldElementType.TUNNELENTER ||
                        worldElement.worldElementType == WorldElementType.TUNNELEXIT).toList();
        if (tunnel.isEmpty()) return;
        ImageView imageView = createImageView(tunnel.get(0).getImagePath(), 0);
        GridPane.setHalignment(imageView, HPos.CENTER);
        mapGrid.add(imageView, entry.getKey().getX() + 1, bounds.getY() - entry.getKey().getY() + 1);
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
}
