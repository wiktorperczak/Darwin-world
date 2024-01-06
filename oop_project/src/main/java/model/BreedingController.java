package model;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class BreedingController {

    public static void breed(WorldMap map){
        Map<Vector2d, List<WorldElement>> allAnimals = map.getAllElements();
        for (Vector2d position : allAnimals.keySet()){
            List<Animal> animals = allAnimals.get(position).stream()
                    .filter(worldElement -> worldElement instanceof Animal)
                    .map(worldElement -> (Animal) worldElement)
                    .sorted(new AnimalComparator()).toList();
            for (int i = 0; i < animals.size() - 1; i += 2){
                Animal animal1 = animals.get(i);
                Animal animal2 = animals.get(i+1);
                if (animal1.getEnergy() < 1 || animal2.getEnergy() < 1) continue;
                Animal children = generateChildren(animal1, animal2);
                //dalszy ciąg
            }
        }
    }

    static Animal generateChildren(Animal parent1, Animal parent2){
        // na tym etapie wiemy, że parent1 jest silniejszy, albo tak samo silny jak parent2
        int energy1 = parent1.getEnergy();
        int energy2 = parent2.getEnergy();
        Random random = new Random();
        boolean takeLeftSide = random.nextInt(2) == 0;
        float ratio = (float) energy1 / (energy1 + energy2);
        List<Integer> childrenGenotype = getNewGenotype(parent1.getGenotype(), parent2.getGenotype(), ratio, takeLeftSide);
        Animal children = new Animal(parent1.getPosition());

//        System.out.println("-----------");
//        System.out.println(parent1.getGenotype() + " Energy: " + energy1);
//        System.out.println(parent2.getGenotype() + " Energy: " + energy2);
//        System.out.println(takeLeftSide);
//        System.out.println(childrenGenotype);
//        System.out.println();
        return new Animal(new Vector2d(0, 0));
    }

    static List<Integer> getNewGenotype(List<Integer> genotype1, List<Integer> genotype2, float ratio, boolean takeLeftSide){
        int n = genotype1.size();
        int len1 = (int) Math.floor(n * ratio);
        int len2 = n - len1;
        List<Integer> resultGenotype = new ArrayList<>();
        if (takeLeftSide){
            resultGenotype.addAll(genotype1.subList(0, len1));
            resultGenotype.addAll(genotype2.subList(len1, n));
        } else {
            resultGenotype.addAll(genotype2.subList(0, len2));
            resultGenotype.addAll(genotype1.subList(len2, n));
        }
        return resultGenotype;
    }
}
