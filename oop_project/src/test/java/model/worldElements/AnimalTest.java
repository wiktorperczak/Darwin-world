package model.worldElements;

import model.application.options.OptionsManager;
import model.map.RectangularMap;
import model.map.utils.MapDirection;
import model.map.utils.Vector2d;
import model.worldElements.Animal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    private RectangularMap map;
    @Test
    public void testCalculateNewRotationWithReverse(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setGenotypeLength(8);
        RectangularMap map = new RectangularMap(5, 5, 1, optionsManager);
        Animal animal = new Animal(map, new Vector2d(2, 2), 1);
        animal.setFacingDirection(MapDirection.NORTH);
        map.place(animal);
        animal.setGenotype(List.of(0, 1, 2, 3, 4, 5, 6, 7));
        assertEquals(MapDirection.NORTH, MapDirection.toMapDirection(animal.getFacingDirection()));
        assertEquals(MapDirection.NORTH, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_EAST, animal.calculateNewRotation());
    }

    @Test
    public void testCalculateNewRotationWithNoReverse(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setGenotypeLength(8);
        optionsManager.setUseReverseGenotype(false);
        RectangularMap map = new RectangularMap(5, 5, 1, optionsManager);
        Animal animal = new Animal(map, new Vector2d(2, 2), 1);
        animal.setFacingDirection(MapDirection.NORTH);
        map.place(animal);
        animal.setGenotype(List.of(0, 1, 2, 3, 4, 5, 6, 7));
        assertEquals(MapDirection.NORTH, MapDirection.toMapDirection(animal.getFacingDirection()));
        assertEquals(MapDirection.NORTH, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_EAST, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH, animal.calculateNewRotation());
        assertEquals(MapDirection.SOUTH_WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.WEST, animal.calculateNewRotation());
        assertEquals(MapDirection.NORTH_WEST, animal.calculateNewRotation());
    }

    @Test
    public void testMoveHorizontally(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setGenotypeLength(2);
        optionsManager.setUseReverseGenotype(false);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal = new Animal(map, new Vector2d(1, 1), 1);
        animal.setFacingDirection(MapDirection.NORTH);
        map.place(animal);
        animal.setGenotype(List.of(0, 0));

        animal.move(map);
        assertEquals(new Vector2d(1, 2), animal.getPosition());
        assertEquals(0, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(1, 2), animal.getPosition());
        assertEquals(4, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        assertEquals(4, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(1, 0), animal.getPosition());
        assertEquals(4, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(1, 0), animal.getPosition());
        assertEquals(0, animal.getFacingDirection());
    }

    @Test
    public void testMoveVertically(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setGenotypeLength(2);
        optionsManager.setUseReverseGenotype(false);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal = new Animal(map, new Vector2d(1, 1), 1);
        animal.setFacingDirection(MapDirection.NORTH);
        map.place(animal);
        animal.setGenotype(List.of(0, 0));
        animal.setFacingDirection(MapDirection.EAST);
        animal.move(map);
        assertEquals(new Vector2d(2, 1), animal.getPosition());
        assertEquals(2, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(0, 1), animal.getPosition());
        assertEquals(2, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        assertEquals(2, animal.getFacingDirection());

        animal.setFacingDirection(MapDirection.WEST);
        animal.move(map);
        assertEquals(new Vector2d(0, 1), animal.getPosition());
        assertEquals(6, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(2, 1), animal.getPosition());
        assertEquals(6, animal.getFacingDirection());
    }

    @Test
    public void testMoveHorizontallyAndVertically(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setGenotypeLength(2);
        optionsManager.setUseReverseGenotype(false);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal = new Animal(map, new Vector2d(1, 1), 1);
        animal.setFacingDirection(MapDirection.NORTH);
        map.place(animal);
        animal.setGenotype(List.of(0, 0));
        animal.setFacingDirection(MapDirection.NORTH_EAST);
        animal.move(map);
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(1, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(0, 2), animal.getPosition());
        assertEquals(5, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(2, 1), animal.getPosition());
        assertEquals(5, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(1, 0), animal.getPosition());
        assertEquals(5, animal.getFacingDirection());
        animal.move(map);
        assertEquals(new Vector2d(0, 0), animal.getPosition());
        assertEquals(1, animal.getFacingDirection());
    }

    @Test
    public void testIsAlive(){
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setAnimalLife(2);
        RectangularMap map = new RectangularMap(2, 2, 1, optionsManager);
        Animal animal = new Animal(map, new Vector2d(1, 1), 1);
        animal.setFacingDirection(MapDirection.NORTH);
        map.place(animal);
        assertTrue(animal.isAlive());
        animal.move(map);
        assertTrue(animal.isAlive());
        animal.move(map);
        assertFalse(animal.isAlive());
    }

    @Test
    public void testCalculateNumberOfDescendants() {
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        RectangularMap map = new RectangularMap(5, 5, 1, optionsManager);
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 0);
        map.place(animal1);
        Animal animal2 = new Animal(map, new Vector2d(2, 2), 1);
        map.place(animal2);
        Animal animal3 = new Animal(map, new Vector2d(3, 3), 2);
        map.place(animal3);
        Animal animal4 = new Animal(map, new Vector2d(4, 4), 3);
        map.place(animal4);
        Animal animal5 = new Animal(map, new Vector2d(5, 5), 4);
        map.place(animal5);
        animal1.getKids().add(animal2);
        animal1.getKids().add(animal3);
        animal2.getKids().add(animal4);
        animal2.getKids().add(animal5);
        animal3.getKids().add(animal4);
        animal1.calculateNumberOfDescendants();
        assertEquals(animal1.getNumberOfDescendants(), 4);
    }

}
