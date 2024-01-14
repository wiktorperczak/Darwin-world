package model.map.utils;

import model.worldElements.Animal;

import java.util.Comparator;
import java.util.Random;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal animal1, Animal animal2) {
        int energy1 = animal1.getEnergy();
        int energy2 = animal2.getEnergy();
        if (energy1 == energy2){
            int days1 = animal1.getNumberOfDaysLived();
            int days2 = animal2.getNumberOfDaysLived();
            if (days1 == days2){
                int numberOfKids1 = animal1.getNumberOfKids();
                int numberOfKids2 = animal2.getNumberOfKids();
                if (numberOfKids1 == numberOfKids2){
                    Random random = new Random();
                    if (random.nextInt(2) == 0){
                        return -1;
                    } else {
                        return 1;
                    }
                }
                return numberOfKids2 - numberOfKids1;
            }
            return days2 - days1;
        }
        return energy2 - energy1;
    }
}
