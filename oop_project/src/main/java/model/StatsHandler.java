package model;

import java.text.DecimalFormat;
import java.util.*;

public class StatsHandler {
    RectangularMap map;
    private int numberOfAnimals;
    private int numberOfGrass;
    private int numberOfFreeSpaces;
    private int mostPopularGen;
    private float averageEnergy;
    private float averageNumberOfKids;
    private float averageLengthOfDeadAnimals;



    private int sumLifeLengthOfDeadAnimals;
    private int numberOfDeadAnimals;
    List<Animal> animals;
    public StatsHandler(RectangularMap map){
        this.map = map;
    }

    public void updateAllStats(){
        animals = getAnimals();
        numberOfAnimals = countAnimals();
        numberOfGrass = countGrass();
        numberOfFreeSpaces = countFreeSpaces();
        averageEnergy = calculateAverageEnergy();
        averageNumberOfKids = calculateAverageNumberOfKids();
        averageLengthOfDeadAnimals = calculateAverageLengthOfDeadAnimals();
        mostPopularGen = calculateMostPopularGen();
    }
    List<Animal> getAnimals(){
        return map.getAnimals().keySet().stream()
                .map(worldElement -> (Animal) worldElement)
                .toList();
    }
    float calculateAverageEnergy(){
        int sum = 0;
        for (Animal animal : animals){
            sum += animal.getEnergy();
        }
        return (float) sum / animals.size();
    }

    int countAnimals(){
        return animals.size();
    }

    int countGrass(){
        return map.getGrassFields().size();
    }

    int countFreeSpaces(){
        Vector2d bounds = map.getBoundaries();
        Map<Vector2d, List<WorldElement>> allElements =  map.getAllElements();
        return ((bounds.getX()+1) * (bounds.getY()+1)) - allElements.size();
    }

    float calculateAverageNumberOfKids(){
        int sum = 0;
        for (Animal animal : animals){
            sum += animal.getNumberOfKids();
        }
        return (float) sum / animals.size();
    }

    float calculateAverageLengthOfDeadAnimals(){
        return (float) sumLifeLengthOfDeadAnimals / numberOfDeadAnimals;
    }

    int calculateMostPopularGen(){
        Map<Integer, Integer> allGens = new HashMap<>();
        for (int i = 0; i < 8; i++){
            allGens.put(i, 0);
        }

        for (Animal animal : animals){
            for (Integer gen : animal.getGenotype()){
                allGens.put(gen, allGens.get(gen) + 1);
            }
        }
        int resultGen = -1;
        int number = -1;
        for (Map.Entry<Integer, Integer> entry : allGens.entrySet()){
            if (entry.getValue() > number){
                resultGen = entry.getKey();
                number = entry.getValue();
            }
        }

        return resultGen;
    }

    public void animalDeceased(Animal animal){
        sumLifeLengthOfDeadAnimals += animal.getNumberOfDaysLived();
        numberOfDeadAnimals += 1;
    }

    public float getAverageEnergy() { return averageEnergy;}

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getNumberOfGrass() {
        return numberOfGrass;
    }

    public int getNumberOfFreeSpaces() {
        return numberOfFreeSpaces;
    }

    public int getMostPopularGen() {
        return mostPopularGen;
    }

    public float getAverageNumberOfKids() {
        return averageNumberOfKids;
    }

    public float getAverageLengthOfDeadAnimals() {
        return averageLengthOfDeadAnimals;
    }

}
