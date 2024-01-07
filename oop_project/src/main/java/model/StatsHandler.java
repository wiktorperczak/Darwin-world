package model;

import java.text.DecimalFormat;
import java.util.*;

public class StatsHandler {
    RectangularMap map;
    private int numberOfAnimals;
    private int numberOfGrass;
    private int numberOfFreeSpaces;
    private int mostPopularGen;
    private double averageEnergy;
    private double averageNumberOfKids;
    private double averageLengthOfDeadAnimals;



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
    double calculateAverageEnergy(){
        int sum = 0;
        for (Animal animal : animals){
            sum += animal.getEnergy();
        }
        return Math.round(((double) sum / animals.size()) * 100.0)/100.0;
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

    double calculateAverageNumberOfKids(){
        int sum = 0;
        for (Animal animal : animals){
            sum += animal.getNumberOfKids();
        }
        return Math.round(((double) sum / animals.size())*100.0)/100.0;
    }

    double calculateAverageLengthOfDeadAnimals(){
        if (numberOfDeadAnimals == 0) return 0;
        return Math.round((((double) sumLifeLengthOfDeadAnimals / numberOfDeadAnimals)*100.0))/100.0;
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

    public double getAverageEnergy() { return averageEnergy;}

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

    public double getAverageNumberOfKids() {
        return averageNumberOfKids;
    }

    public double getAverageLengthOfDeadAnimals() {
        return averageLengthOfDeadAnimals;
    }

}
