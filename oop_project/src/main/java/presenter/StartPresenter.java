package presenter;

import model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class StartPresenter {
    public TextField height;
    public TextField width;

    public TextField animalLife;
    public TextField genotypeLength;
    public TextField grassEnergy;
    public TextField numberOfTunnels;

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

        OptionsManager optionsManager = OptionsManager.getInstance();
        optionsManager.setWidth(Integer.parseInt(width.getText()));
        optionsManager.setHeight(Integer.parseInt(height.getText()));
        optionsManager.setAnimalLife(Integer.parseInt(animalLife.getText()));
        optionsManager.setGenotypeLength(Integer.parseInt(genotypeLength.getText()));
        optionsManager.setGrassEnergy(Integer.parseInt(grassEnergy.getText()));
        optionsManager.setNumberOfTunnels(Integer.parseInt(numberOfTunnels.getText()));

        List<Vector2d> positions = List.of(new Vector2d(1,1), new Vector2d(2, 2));
//        List<Vector2d> positions = List.of();
        WorldMap map = new RectangularMap(optionsManager.getWidth(), optionsManager.getHeight());


        SimulationPresenter simulationPresenter = loader.getController();
        map.registerObserver(simulationPresenter);

        Simulation simulation = new Simulation(positions, map);
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
    }
}
