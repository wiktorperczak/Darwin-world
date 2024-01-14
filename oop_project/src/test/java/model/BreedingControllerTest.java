package model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BreedingControllerTest {

    @Test
    public void testBreeding(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setMinimalEnergyToBreed(5);
        optionsManager.setEnergyLossOnBreed(2);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 1);
        Animal animal2 = new Animal(map, new Vector2d(1, 1), 1);
        map.place(animal1);
        map.place(animal2);
        animal1.setEnergy(10);
        animal2.setEnergy(10);
        map.updateAllElements();
        BreedingController.breed(map);
        assertEquals(3, map.getAnimals().size());
        assertEquals(8, animal1.getEnergy());
        assertEquals(8, animal2.getEnergy());
        assertEquals(1, animal1.getNumberOfKids());
        assertEquals(1, animal2.getNumberOfKids());
        Map<WorldElement, Vector2d> animals = map.getAnimals();
        boolean check = false;
        for (WorldElement worldElement : animals.keySet()){
            if (((Animal) worldElement).getEnergy() == 4) check = true;
        }
        assertTrue(check);
    }

    @Test
    public void testBreeding2(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setMinimalEnergyToBreed(5);
        optionsManager.setEnergyLossOnBreed(2);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 1);
        Animal animal2 = new Animal(map, new Vector2d(1, 1), 1);
        Animal animal3 = new Animal(map, new Vector2d(1, 1), 1);
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        animal1.setEnergy(10);
        animal2.setEnergy(10);
        animal3.setEnergy(10);
        animal1.setNumberOfDaysLived(3);
        animal2.setNumberOfDaysLived(4);
        animal3.setNumberOfDaysLived(5);
        map.updateAllElements();
        BreedingController.breed(map);
        assertEquals(4, map.getAnimals().size());
        assertEquals(10, animal1.getEnergy());
        assertEquals(8, animal2.getEnergy());
        assertEquals(8, animal3.getEnergy());
        assertEquals(10, animal1.getEnergy());
        assertEquals(1, animal2.getNumberOfKids());
        assertEquals(1, animal3.getNumberOfKids());
        Map<WorldElement, Vector2d> animals = map.getAnimals();
        boolean check = false;
        for (WorldElement worldElement : animals.keySet()){
            if (((Animal) worldElement).getEnergy() == 4) check = true;
        }
        assertTrue(check);
    }

    @Test
    public void testNumberOfGensToMutate(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setMinimalEnergyToBreed(5);
        optionsManager.setEnergyLossOnBreed(2);
        optionsManager.setGenotypeLength(5);
        optionsManager.setMaxGensToMutate(3);
        optionsManager.setMinGensToMutate(3);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 1);
        Animal animal2 = new Animal(map, new Vector2d(1, 1), 1);
        Animal animal3 = new Animal(map, new Vector2d(1, 1), 1);
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        animal1.setEnergy(10);
        animal2.setEnergy(10);
        animal3.setEnergy(10);
        animal1.setNumberOfDaysLived(3);
        animal2.setNumberOfDaysLived(4);
        animal3.setNumberOfDaysLived(5);
        map.updateAllElements();
        BreedingController.breed(map);

        assertEquals(1, animal3.getNumberOfKids());
        assertEquals(1, animal2.getNumberOfKids());
        Map<WorldElement, Vector2d> animals = map.getAnimals();
        int it = 0;
        for (WorldElement worldElement : animals.keySet()){
            Animal kid = (Animal) worldElement;
            if (kid.getEnergy() == 4) {
                for (int i = 0; i < optionsManager.getGenotypeLength(); i++){
                    if (Objects.equals(kid.getGenotype().get(i), animal3.getGenotype().get(i)) ||
                            Objects.equals(kid.getGenotype().get(i), animal2.getGenotype().get(i))){
                        it += 1;
                    }
                }
            }
        }
        assertTrue(it >= optionsManager.getGenotypeLength()-optionsManager.getMaxGensToMutate());
    }
}
