package model;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class BreedingController {
    static OptionsManager optionsManager = OptionsManager.getInstance();

    public static void breed(WorldMap map){
        Map<Vector2d, List<WorldElement>> allAnimals = map.getAllElements();
        int numberOfChildrenAdded = 0;
        for (Vector2d position : allAnimals.keySet()){
            List<Animal> animals = allAnimals.get(position).stream()
                    .filter(worldElement -> worldElement instanceof Animal)
                    .map(worldElement -> (Animal) worldElement)
                    .toList();
            for (int i = 0; i < animals.size() - 1; i += 2){
                Animal animal1 = animals.get(i);
                Animal animal2 = animals.get(i+1);

                if (animal1.getEnergy() < optionsManager.getMinimalEnergyToBreed() ||
                        animal2.getEnergy() < optionsManager.getMinimalEnergyToBreed()) continue;
                Animal children = generateChildren(animal1, animal2);
                map.place(children);
                numberOfChildrenAdded += 1;
            }
        }
        if (numberOfChildrenAdded > 0){
            ((RectangularMap) map).mapChanged("Zwierzaki się rozmnożyły");
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
//        System.out.println("-----------");
//        System.out.println(parent1.getGenotype() + " Energy: " + energy1);
//        System.out.println(parent2.getGenotype() + " Energy: " + energy2);
//        System.out.println(takeLeftSide);
//        System.out.println(childrenGenotype);
//        System.out.println();
        Animal children = new Animal(parent1.getPosition());
        children.setEnergy(2 * optionsManager.getEnergyLossOnBreed());
        children.setGenotype(childrenGenotype);
        parent1.addEnergy(-optionsManager.getEnergyLossOnBreed());
        parent2.addEnergy(-optionsManager.getEnergyLossOnBreed());
        return children;
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
        return mutate(resultGenotype);
    }

    static List<Integer> mutate(List<Integer> genotype){
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genotype.size(); i++){
            indexes.add(i);
        }
        for (int i = 0; i < optionsManager.getGensToMutate(); i++){
            Random random = new Random();
            int temp = random.nextInt(indexes.size());
            int index = indexes.get(temp);
            indexes.remove(temp);
            ArrayList<Integer> gens = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7));
            gens.remove(genotype.get(index));
            genotype.remove(index);
            genotype.add(index, gens.get(random.nextInt(7)));
        }
        return genotype;
    }
}
