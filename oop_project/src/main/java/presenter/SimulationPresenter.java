package presenter;

import model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

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

        for (Map.Entry<Vector2d, List<WorldElement>> entry : map.getAllElements().entrySet()) {
            label = new Label(entry.getValue().get(0).toString());
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, entry.getKey().getX() + 1,
                    bounds.getY() - (entry.getKey().getY()) + 1);
        }
    }

    private void clearGrid(){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
}
