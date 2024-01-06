package model;

import java.util.List;
import java.util.Map;

public interface WorldMap {
    void place(Animal animal);
    void move(Animal animal);
    boolean isOccupied(Vector2d position);
    WorldElement objectAt(Vector2d position);
    Map<Vector2d, List<WorldElement>> getElements();
    Vector2d getBoundaries();
    void updateAllElements();
    int getId();
    void registerObserver(MapChangeListener obs);

    void unregisterObserver(MapChangeListener obs);
}
