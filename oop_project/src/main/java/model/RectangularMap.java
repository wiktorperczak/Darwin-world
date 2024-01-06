package model;

import java.util.*;

public class RectangularMap implements WorldMap{
    private final Map<MapChangeListener, MapChangeListener> observers = new HashMap<>();
    public OptionsManager optionsManager;
    Map<WorldElement, Vector2d> animals = new HashMap<>();
    Map<WorldElement, Vector2d> grassFields = new HashMap<>();
    List<List<Boolean>> isGrass = new ArrayList<>();
    Map<WorldElement, Vector2d> tunnelEnters = new HashMap<>();
    Map<WorldElement, Vector2d> tunnelExits = new HashMap<>();
    Map<Vector2d, List<WorldElement>> allElements = new HashMap<>();
    Map<Vector2d, TunnelEnter> tunnels = new HashMap<>();
    private int width;
    private int height;
    protected UUID id;

    public RectangularMap(int width, int height, OptionsManager optionsManager) {
        this.width = width;
        this.height = height;
        this.id = generateId();
        this.optionsManager = optionsManager;
        initializeIsGrass();
        initializeTunnels();

    }

    void initializeIsGrass(){
        for (int i = 0; i <= height; i++) {
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j <= width; j++) { row.add(false); }
            isGrass.add(row);
        }
    }

    void initializeTunnels(){
        TunnelEnter tunnelEnter = new TunnelEnter(new Vector2d(3, 3));
        tunnelEnters.put(tunnelEnter, tunnelEnter.getPosition());
        TunnelExit tunnelExit = tunnelEnter.getTunnelExit();
        tunnelExits.put(tunnelExit, tunnelExit.getPosition());

        for (WorldElement worldElement : tunnelEnters.keySet()){
            tunnels.put(worldElement.getPosition(), (TunnelEnter) worldElement);
        }
    }

    @Override
    public void place(Animal animal) {
        System.out.println("Animal added");
        animals.put(animal, animal.getPosition());
    }

    @Override
    public void move(Animal animal) {
        animals.remove(animal);
        animal.move(this);
        animals.put(animal, animal.getPosition());
        updateAllElements();
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return allElements.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (allElements.containsKey(position)) return allElements.get(position).get(0);
        return null;
    }

    @Override
    public Map<Vector2d, List<WorldElement>> getAllElements() {
        return allElements;
    }

    public void updateAllElements() {
        Map<Vector2d, List<WorldElement>> elements = new HashMap<>();
        elements = addNewValuesToElements(elements, animals);
        elements = addNewValuesToElements(elements, tunnelEnters);
        elements = addNewValuesToElements(elements, tunnelExits);
        elements = addNewValuesToElements(elements, grassFields);
        allElements = sortAnimals(elements);
    }

    Map<Vector2d, List<WorldElement>> addNewValuesToElements(Map<Vector2d, List<WorldElement>> elements,
                                                                    Map<WorldElement, Vector2d> worldElements){
        for (WorldElement worldElement : worldElements.keySet()){
            Vector2d pos = worldElement.getPosition();
            if (elements.containsKey(pos)){
                List<WorldElement> tempList = elements.get(pos);
                tempList.add(worldElement);
                elements.put(pos, tempList);
            } else {
                elements.put(pos, new ArrayList<>(List.of(worldElement)));
            }
        }
        return elements;
    }

    Map<Vector2d, List<WorldElement>> sortAnimals(Map<Vector2d, List<WorldElement>> elements){
        for (Vector2d position : elements.keySet()){
            List<WorldElement> animals = elements.get(position).stream()
                    .filter(worldElement -> worldElement instanceof Animal)
                    .map(worldElement -> (Animal) worldElement)
                    .sorted(new AnimalComparator())
                    .map(worldElement -> (WorldElement) worldElement)
                    .toList();
            List<WorldElement> restWorldElements = elements.get(position).stream()
                    .filter(worldElement -> !(worldElement instanceof Animal))
                    .toList();
            List<WorldElement> worldElements = new ArrayList<>();
            worldElements.addAll(animals);
            worldElements.addAll(restWorldElements);
            elements.put(position, worldElements);
        }
        return elements;
    }

    public void registerObserver(MapChangeListener obs){
        observers.put(obs, obs);
    }

    public void unregisterObserver(MapChangeListener obs){
        observers.remove(obs);
    }

    protected void mapChanged(String message){
        for (MapChangeListener obs: observers.values()){
            obs.mapChanged(this, message);
        }
    }

    public void addNewGrassField(int x, int y) {
        isGrass.get(y).set(x, true);
        Vector2d position = new Vector2d(x, y);
        grassFields.put(new Grass(position), position);
    }

    public void deleteEatenGrass() {

    }

    @Override
    public Vector2d getBoundaries() {
        return new Vector2d(width, height);
    }

    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw();
    }

    UUID generateId(){
        return UUID.randomUUID();
    }
    @Override
    public UUID getId() { return id;}

    public Map<WorldElement, Vector2d> getAnimals(){
        return animals;
    }
    public Map<WorldElement, Vector2d> getGrassFields(){
        return grassFields;
    }
    public boolean getIsGrass(int x, int y) { return isGrass.get(y).get(x); }
    public void setIsGrassValue(int x, int y, boolean value) { isGrass.get(y).set(x, value); }
    public Map<Vector2d, TunnelEnter> getTunnels(){
        return tunnels;
    }

    public void removeAnimal(Animal animalToRemove){
        animals.remove(animalToRemove);
    }
}
