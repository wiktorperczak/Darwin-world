package model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WorldMap {
    void place(Animal animal);
    void move(Animal animal);
    boolean isOccupied(Vector2d position);
    WorldElement objectAt(Vector2d position);
    Map<Vector2d, List<WorldElement>> getAllElements();
    Vector2d getBoundaries();
    void updateAllElements();
    UUID getId();
}
