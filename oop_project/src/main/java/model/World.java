package model;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static void main(String[] args) throws InterruptedException{
        setParameters();

        ConsoleMapDisplay obs = new ConsoleMapDisplay();
        List<Simulation> simulations = new ArrayList<>();
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(0, 0));

        for (int i = 0; i < 1; i++){
            RectangularMap map = new RectangularMap(5, 5, i);
            Simulation simulationRectangularMap = new Simulation(positions, map);
            map.registerObserver(obs);
            simulations.add(simulationRectangularMap);
        }

        SimulationEngine simulationEngine = new SimulationEngine(simulations);
        simulationEngine.runAsyncInThreadPool();
        simulationEngine.awaitSimulationsEnd();
        System.out.println("System zakończył działanie");
    }

    static void setParameters(){
        OptionsManager optionsManager = OptionsManager.getInstance();
        optionsManager.setAnimalLife(5);
        optionsManager.setGenotypeLength(5);
    }
}
