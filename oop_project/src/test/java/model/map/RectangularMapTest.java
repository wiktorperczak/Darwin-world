package model.map;

import model.application.options.OptionsManager;
import model.map.RectangularMap;
import model.map.utils.Vector2d;
import model.worldElements.Animal;
import model.worldElements.WorldElement;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class RectangularMapTest {
    @Test
    public void testIsEquatorPosition() {
        OptionsManager optionsManager = new OptionsManager();
        RectangularMap map = new RectangularMap(10, 10, 1, optionsManager);
        assertTrue(map.isEquatorPosition(5));
        assertTrue(map.isEquatorPosition(4));
        assertFalse(map.isEquatorPosition(6));

        RectangularMap map1 = new RectangularMap(2, 2, 2, optionsManager);
        assertTrue(map1.isEquatorPosition(1));
        assertFalse(map1.isEquatorPosition(0));
        assertFalse(map1.isEquatorPosition(2));
    }

    @Test
    public void testUpdateAllElements() {
        OptionsManager optionsManager = new OptionsManager();
        optionsManager.generateBasicValues();
        optionsManager.setNumberOfTunnels(2);
        RectangularMap map = new RectangularMap(10, 10, 0, optionsManager);

        map.initializeTunnels();

        Animal animal1 = new Animal(map, new Vector2d(1, 1), 0);
        map.place(animal1);
        Animal animal2 = new Animal(map, new Vector2d(2, 2), 1);
        map.place(animal2);
        Animal animal3 = new Animal(map, new Vector2d(3, 3), 2);
        map.place(animal3);

        map.updateAllElements();
        int numberOfAllElements = 0;
        for (Map.Entry<Vector2d, List<WorldElement>> elem : map.getAllElements().entrySet()) {
            numberOfAllElements += elem.getValue().size();
        }
        assertEquals(numberOfAllElements, 7);
    }
}
