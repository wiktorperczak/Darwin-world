package model.simulation;

import model.application.options.OptionsManager;
import model.map.RectangularMap;
import model.map.utils.Vector2d;
import model.simulation.Simulation;
import model.simulation.SimulationEngine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    @Test
    public void runTest() {
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setNumberOfAnimals(2);
        optionsManager.setUseTunnels(false);
        optionsManager.setNumberOfGrassPerDay(0);
        optionsManager.setStartingGrassNumber(0);
        optionsManager.setAnimalLife(5);
        RectangularMap map = new RectangularMap(10, 10, 1, optionsManager);
        List<Vector2d> positions = List.of(new Vector2d(0, 0), new Vector2d(10, 10));

        Simulation simulation = new Simulation(positions, map);

        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();

        map.updateAllElements();
        assertEquals(2, map.getAllElements().size());
    }
}
