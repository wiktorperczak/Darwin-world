package model.map.visualization;

import model.map.utils.MapChangeListener;
import model.map.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    public synchronized void mapChanged(WorldMap worldMap, String message){
        System.out.println("ID: " + worldMap.getId());
        System.out.println("Ilość zwierzaków: " + worldMap.getAnimals().size());
        System.out.println(message);
        System.out.println(worldMap);
    }
}
