package model;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static void main(String[] args) throws InterruptedException{
        OptionsManager optionsManager = setParameters();

        ConsoleMapDisplay obs = new ConsoleMapDisplay();
        List<Simulation> simulations = new ArrayList<>();
        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(0, 0));

//        for (int i = 0; i < 3; i++){
//            RectangularMap map = new RectangularMap(5, 5, i);
//            Simulation simulationRectangularMap = new Simulation(positions, map);
//            map.registerObserver(obs);
//            simulations.add(simulationRectangularMap);
//        }
//        SimulationEngine simulationEngine = new SimulationEngine(simulations);
//        simulationEngine.runAsyncInThreadPool();
//        simulationEngine.awaitSimulationsEnd();
        RectangularMap map = new RectangularMap(3, 3, optionsManager);
        map.registerObserver(obs);
        Simulation simulation = new Simulation(positions, map);
        simulation.run();
        System.out.println("System zakończył działanie");
    }

    static OptionsManager setParameters(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.setAnimalLife(5);
        optionsManager.setGenotypeLength(5);
        optionsManager.setEnergyLossOnBreed(1);
        optionsManager.setMinimalEnergyToBreed(1);
        optionsManager.setMaxGensToMutate(3);
        optionsManager.setMinGensToMutate(3);
        optionsManager.setGrassEnergy(2);
        return optionsManager;
    }
}
