package model.simulation;

import model.application.options.CsvHandler;
import model.map.RectangularMap;
import model.map.WorldMap;
import model.map.utils.BreedingController;
import model.map.utils.Vector2d;
import model.worldElements.Animal;
import model.worldElements.WorldElement;
import model.worldElements.WorldElementType;

import java.util.*;


public class Simulation implements Runnable{
    private RectangularMap map;
    Random random = new Random();
    private final Object GUI_INITIALIZATION_MONITOR = new Object();
    private boolean pauseThreadFlag = false;
    private final int simulationSpeed;


    public Simulation(List<Vector2d> animalsStartingPos, WorldMap map){
        this.map = (RectangularMap) map;
        for (Vector2d position : animalsStartingPos) {
            this.map.place(new Animal(this.map, position, this.map.getNumberOfAnimalsAndIncrement()));
        }
        simulationSpeed = (11 - this.map.optionsManager.getSimulationSpeed()) * 100;
        this.map.mapChanged("Zwierzaki się ustawiły");
    }

    public void run() {
        CsvHandler csvHandler = new CsvHandler(map);
        String filePath = "";
        if (map.optionsManager.getStatsToCsv()) {
            filePath = "statystyki/" + map.optionsManager.getStatsName();
            csvHandler.createCsvFile(filePath);
            addGrass(map.optionsManager.getStartingGrassNumber());
        }

        while(!map.getAnimals().isEmpty()) {
            checkForPaused();
            removeDeadBodies();
            if (map.getAnimals().isEmpty()){
                break;
            }
            moveAnimals();
            eatingGrass();
            breed();
            addGrass(map.optionsManager.getNumberOfGrassPerDay());
            map.updateAllElements();
            map.mapChanged("Zwierzaki sie ruszyly");
            map.countAllStats();
            if (map.optionsManager.getStatsToCsv()) { csvHandler.appendRowToCsv(filePath); }
            map.anotherDaySimulated();
            try {
                Thread.sleep(simulationSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        map.countAllStats();
        map.mapChanged("Wszystkie zwierzaki umarly");
    }

    void removeDeadBodies(){
        List<WorldElement> animals = new ArrayList<>(map.getAnimals().keySet());
        List<Animal> animalsToRemove = new ArrayList<>();
        if (animals.isEmpty()) return;
        for (WorldElement worldElement : animals){
            Animal animal = (Animal) worldElement;
            if (!animal.isAlive()){
                animalsToRemove.add(animal);
            }
        }
        for (Animal animal : animalsToRemove){
            map.removeAnimal(animal);
        }
        if (!animalsToRemove.isEmpty()) {
            map.updateAllElements();
        }
    }
    void moveAnimals(){
        List<WorldElement> animals = new ArrayList<>(map.getAnimals().keySet());
        if (animals.isEmpty()) return;
        for (WorldElement worldElement : animals) {
            Animal animal = (Animal) worldElement;
            map.move(animal);
        }
        map.updateAllElements();
    }
    void breed(){
        BreedingController.breed(map);
    }

    void eatingGrass() {
        for (Map.Entry<Vector2d, List<WorldElement>> entry : map.getAllElements().entrySet()) {
            Vector2d position = entry.getKey();
            WorldElement element = entry.getValue().get(0);
            if (element.worldElementType == WorldElementType.ANIMAL) {
                if (map.isEquatorPosition(position.getY()) && !map.getEquatorEmptyFields().contains(position)) {
                    ((Animal) element).addEnergy(map.optionsManager.getGrassEnergy());
                    map.getEquatorEmptyFields().add(position);
                    ((Animal) element).addGrassEaten();
                }
                if (!map.isEquatorPosition(position.getY()) && !map.getNonEquatorEmptyFields().contains(position)) {
                    ((Animal) element).addEnergy(map.optionsManager.getGrassEnergy());
                    map.getNonEquatorEmptyFields().add(position);
                }
            }
        }

        Iterator<Map.Entry<WorldElement, Vector2d>> iterator = map.getGrassFields().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WorldElement, Vector2d> entry = iterator.next();
            Vector2d position = entry.getValue();
            if (map.getEquatorEmptyFields().contains(position) || map.getNonEquatorEmptyFields().contains(position)) {
                iterator.remove();
            }
        }

        map.updateAllElements();
    }

    void addGrass(int numberToAdd) {
        for (int i = 0; i < numberToAdd; i++) {
            int randomNumber = random.nextInt(100) + 1;
            if (randomNumber <= 80) {
                if (!map.getEquatorEmptyFields().isEmpty()) {
                    map.generateRandomPosition(map.getEquatorEmptyFields());
                }
                else if (!map.getNonEquatorEmptyFields().isEmpty()) {
                    map.generateRandomPosition(map.getNonEquatorEmptyFields());
                }
            }
            else {
                if (!map.getNonEquatorEmptyFields().isEmpty()) {
                    map.generateRandomPosition(map.getNonEquatorEmptyFields());
                }
                else if (!map.getEquatorEmptyFields().isEmpty()) {
                    map.generateRandomPosition(map.getEquatorEmptyFields());
                }
            }
        }
    }

    private void checkForPaused() {
        synchronized (GUI_INITIALIZATION_MONITOR) {
            while (pauseThreadFlag) {
                try {
                    GUI_INITIALIZATION_MONITOR.wait();
                } catch (Exception e) {}
            }
        }
    }

    public void stopSimulation() {
        map.killAllAnimals();
    }

    public void pauseSimulation() {
        pauseThreadFlag = true;
    }

    public void unPauseSimulation(){
        synchronized(GUI_INITIALIZATION_MONITOR) {
            pauseThreadFlag = false;
            GUI_INITIALIZATION_MONITOR.notify();
        }
    }
}
