package model.map;

import model.map.utils.MapChangeListener;
import model.application.options.StatsHandler;
import model.map.utils.Vector2d;
import model.worldElements.Animal;
import model.worldElements.WorldElement;

import java.util.List;
import java.util.Map;

public interface WorldMap {
    void place(Animal animal);
    void move(Animal animal);
    boolean isOccupied(Vector2d position);
    WorldElement objectAt(Vector2d position);
    Map<Vector2d, List<WorldElement>> getAllElements();
    Vector2d getBoundaries();
    void updateAllElements();
    Map<WorldElement, Vector2d> getAnimals();
    int getId();
    void registerObserver(MapChangeListener obs);

    void unregisterObserver(MapChangeListener obs);
    StatsHandler getStatsHandler();
    Integer getDaysSimulated();
    boolean isEquatorPosition(int y);
}
