package model.application.options;

import model.map.RectangularMap;
import model.map.utils.Vector2d;
import model.worldElements.Animal;
import model.worldElements.WorldElement;

import java.util.*;

public class StatsHandler {
    private RectangularMap map;
    private int numberOfAnimals;
    private int numberOfGrass;
    private int numberOfFreeSpaces;
    private List<Integer> mostPopularGenotype;
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
        mostPopularGenotype = calculateMostPopularGen();
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

    List<Integer> calculateMostPopularGen(){
        Map<List<Integer>, Integer> gensMap = new HashMap<>();
//        for (int i = 0; i < 8; i++){
//            allGens.put(i, 0);
//        }

        for (Animal animal : animals){
            List<Integer> animalsGenotype = animal.getGenotype();
            if (gensMap.containsKey(animalsGenotype)){
                gensMap.put(animalsGenotype, gensMap.get(animalsGenotype) + 1);
            } else {
                gensMap.put(animalsGenotype, 1);
            }
        }
        List<Integer> resultGenotype = new ArrayList<>();
        int maxNumber = -1;
        for (Map.Entry<List<Integer>, Integer> entry : gensMap.entrySet()){
            if (entry.getValue() > maxNumber){
                resultGenotype = entry.getKey();
                maxNumber = entry.getValue();
            }
        }

        return resultGenotype;
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

    public List<Integer> getMostPopularGenotype() {
        return mostPopularGenotype;
    }

    public double getAverageNumberOfKids() {
        return averageNumberOfKids;
    }

    public double getAverageLengthOfDeadAnimals() {
        return averageLengthOfDeadAnimals;
    }

}
