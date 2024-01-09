package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalComparatorTest {

    @Test
    public void testIsAlive(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setAnimalLife(2);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        AnimalComparator animalComparator = new AnimalComparator();
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 1);
        Animal animal2 = new Animal(map, new Vector2d(1, 1), 2);
        animal1.setEnergy(10);
        animal2.setEnergy(9);
        assertTrue(animalComparator.compare(animal1, animal2) < 0);
        animal2.setEnergy(10);

        animal1.setNumberOfDaysLived(10);
        animal2.setNumberOfDaysLived(9);
        assertTrue(animalComparator.compare(animal1, animal2) < 0);

        animal1.setNumberOfKids(2);
        animal2.setNumberOfKids(3);
        assertTrue(animalComparator.compare(animal1, animal2) < 0);

        animal2.setNumberOfDaysLived(10);
        assertTrue(animalComparator.compare(animal1, animal2) > 0);

        animal2.setEnergy(11);
        assertTrue(animalComparator.compare(animal1, animal2) > 0);

        animal1.setEnergy(100);
        assertTrue(animalComparator.compare(animal1, animal2) < 0);
    }
}
