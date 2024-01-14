package model.application;

import model.map.visualization.ConsoleMapDisplay;
import model.application.options.OptionsManager;
import model.map.utils.Vector2d;
import model.map.RectangularMap;
import model.simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static void main(String[] args) throws InterruptedException{
        OptionsManager optionsManager = setParameters();

        ConsoleMapDisplay obs = new ConsoleMapDisplay();
        List<Simulation> simulations = new ArrayList<>();
        List<Vector2d> positions = List.of(new Vector2d(1, 1), new Vector2d(0, 0));

        RectangularMap map = new RectangularMap(3, 3, 0, optionsManager);
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
