package model.map.utils;
import model.map.utils.Vector2d;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class Vector2dTest {
    @Test
    public void equalsTest() {
        Vector2d vec1 = new Vector2d(2, -1);
        Vector2d vec2 = new Vector2d(2, -1);
        Vector2d vec3 = new Vector2d(3, 6);

        // check if same objects are equal
        Assertions.assertTrue(vec1.equals(vec1));

        // check if objects with identical parameters are equal
        Assertions.assertTrue(vec1.equals(vec2));

        // check if objects with different parameters are equal
        Assertions.assertFalse(vec1.equals(vec3));

        // check if object is equal to null
        Assertions.assertFalse(vec1.equals(null));

        // check if object is euqal to diffetent type (int)
        Assertions.assertFalse(vec1.equals(4));

    }

    @Test
    public void toStringTest() {
        Vector2d vec1 = new Vector2d(2, -1);

        Assertions.assertEquals("(2, -1)", vec1.toString());
    }

    @Test
    public void addTest() {
        Vector2d vec1 = new Vector2d(2, -1);
        Vector2d vec3 = new Vector2d(3, 6);

        Assertions.assertEquals(new Vector2d(5, 5), vec1.add(vec3));
    }

    @Test
    public void oppositeTest() {
        Vector2d vec1 = new Vector2d(2, -1);
        Vector2d vec4 = new Vector2d(0, 0);

        Assertions.assertEquals(new Vector2d(-2, 1), vec1.opposite());
        Assertions.assertEquals(new Vector2d(0, 0), vec4.opposite());
    }
}
