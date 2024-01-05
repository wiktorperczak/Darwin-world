package model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updatesNo = 1;

    public synchronized void mapChanged(WorldMap worldMap, String message){
//        System.out.println("ID: " + worldMap.getId());
        System.out.println("Number of updates: " + updatesNo);
        System.out.println(message);
        System.out.println(worldMap);
        updatesNo++;
    }
}
