package model;

import java.util.List;

public class World {

    public static void main(String[] args){
        setParameters();
        RectangularMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(List.of(new Vector2d(2, 2), new Vector2d(0, 0)), map);
        simulation.run();
    }

    static void setParameters(){
        OptionsManager optionsManager = OptionsManager.getInstance();
        optionsManager.setAnimalLife(5);
        optionsManager.setGenotypeLength(5);
    }
}
