package model;

import java.util.*;

public class RectangularMap implements WorldMap{
    private final Map<MapChangeListener, MapChangeListener> observers = new HashMap<>();
    public OptionsManager optionsManager;
    public StatsHandler statsHandler;
    private Map<WorldElement, Vector2d> animals = new HashMap<>();
    private Map<WorldElement, Vector2d> grassFields = new HashMap<>();
    private Set<Vector2d> equatorEmptyFields = new HashSet<>();
    private Set<Vector2d> nonEquatorEmptyFields = new HashSet<>();
    private Map<WorldElement, Vector2d> tunnelEnters = new HashMap<>();
    private Map<WorldElement, Vector2d> tunnelExits = new HashMap<>();
    private Map<Vector2d, List<WorldElement>> allElements = new HashMap<>();
    private Map<Vector2d, TunnelEnter> tunnels = new HashMap<>();

    private int daysSimulated;
    private final int width;
    private final int height;
    protected int id;
    private int numberOfAllAnimals;
    public List<Boolean> animalIdVisited = new ArrayList<>();
    private final Random random = new Random();


    public RectangularMap(int width, int height, int id, OptionsManager optionsManager) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.optionsManager = optionsManager;
        initializeIsGrass();
        daysSimulated = 0;
        if (optionsManager.getUseTunnels()) { initializeTunnels(); }
        statsHandler = new StatsHandler(this);
    }

    public boolean isEquatorPosition(int y) {
        int mod = (height + 1) % 5, equatorWidth = (height + 1) / 5;
        if (mod == 3 || mod == 4) equatorWidth += 1;

        int lower_bound = (height + 1 - equatorWidth) / 2;
        int upper_bound = lower_bound + equatorWidth - 1;
        return (y >= lower_bound && y <= upper_bound);
    }

    void initializeIsGrass(){
        for (int y = 0; y <= height; y++) {
            boolean isEquator = isEquatorPosition(y);
            for (int x = 0; x <= width; x++) {
                Vector2d position = new Vector2d(x, y);
                if (isEquator) { equatorEmptyFields.add(position); }
                else { nonEquatorEmptyFields.add(position); }
            }
        }
    }

    void initializeTunnels(){
        List<Integer> coordinates = new ArrayList<>();
        for (int i = 0; i < (height + 1) * (width + 1); i++) { coordinates.add(i); }
        Collections.shuffle(coordinates);

        for (int i = 0; i < 2 * optionsManager.getNumberOfTunnels(); i += 2) {
            int enterX = coordinates.get(i) % (width + 1);
            int enterY = coordinates.get(i) / (width + 1);
            int exitX = coordinates.get(i + 1) % (width + 1);
            int exitY = coordinates.get(i + 1) / (width + 1);

            TunnelEnter tunnelEnter = new TunnelEnter(new Vector2d(enterX, enterY));
            TunnelEnter tunnelExit = new TunnelEnter(new Vector2d(exitX, exitY));
            tunnelEnter.setTunnelExit(tunnelExit);
            tunnelExit.setTunnelExit(tunnelEnter);
            tunnelEnters.put(tunnelEnter, tunnelEnter.getPosition());
            tunnelExits.put(tunnelExit, tunnelExit.getPosition());

            tunnels.put(tunnelEnter.getPosition(), tunnelEnter);
            tunnels.put(tunnelExit.getPosition(), tunnelExit);

        }
    }

    @Override
    public void place(Animal animal) {
        animals.put(animal, animal.getPosition());
        animalIdVisited.add(false);
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
        elements = addNewValuesToElements(elements, grassFields);
        elements = addNewValuesToElements(elements, tunnelEnters);
        elements = addNewValuesToElements(elements, tunnelExits);
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
                    .filter(worldElement -> worldElement.worldElementType == WorldElementType.ANIMAL)
                    .map(worldElement -> (Animal) worldElement)
                    .sorted(new AnimalComparator())
                    .map(worldElement -> (WorldElement) worldElement)
                    .toList();
            List<WorldElement> restWorldElements = elements.get(position).stream()
                    .filter(worldElement -> !(worldElement.worldElementType == WorldElementType.ANIMAL))
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
        Vector2d position = new Vector2d(x, y);
        if (isEquatorPosition(y)) { equatorEmptyFields.remove(position); }
        else { nonEquatorEmptyFields.remove(position); }
        grassFields.put(new Grass(position), position);
    }

    public void generateRandomPosition(Set<Vector2d> setPosition) {
        int range = setPosition.size();
        int rand = random.nextInt(range), i = 0;
        Vector2d field = new Vector2d(0, 0);
        for (Vector2d position : setPosition) {
            if (i == rand) {
                field = position;
                break;
            }
            i += 1;
        }
        addNewGrassField(field.getX(), field.getY());
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

    @Override
    public int getId() { return id;}

    public Map<WorldElement, Vector2d> getAnimals(){
        return animals;
    }
    public Map<WorldElement, Vector2d> getGrassFields(){
        return grassFields;
    }
    public Map<Vector2d, TunnelEnter> getTunnels(){
        return tunnels;
    }

    public void removeAnimal(Animal animalToRemove){
        animals.remove(animalToRemove);
        statsHandler.animalDeceased(animalToRemove);
        animalToRemove.setDayOfDeath(daysSimulated);
    }

    public void killAllAnimals(){
        animals = new HashMap<>();
        updateAllElements();
    }

    public void countAllStats(){
        statsHandler.updateAllStats();
    }

    public StatsHandler getStatsHandler(){
        return statsHandler;
    }

    public int getNumberOfAllAnimals() { return numberOfAllAnimals; }
    public void setNumberOfAllAnimals(int numberOfAllAnimals) { this.numberOfAllAnimals = numberOfAllAnimals; }
    public int getNumberOfAnimalsAndIncrement() {
        numberOfAllAnimals += 1;
        return numberOfAllAnimals - 1;
    }

    public boolean getAnimalIdVisited(int id) { return animalIdVisited.get(id); }
    public void setAnimalIdVisited(int id, boolean value) { animalIdVisited.set(id, value); }
    public void resetAnimalIdVisited() { Collections.fill(animalIdVisited, false); }

    public void anotherDaySimulated() {
        daysSimulated += 1;
    }

    public Integer getDaysSimulated(){
        return daysSimulated;
    }
    public void clearAnimaIdVisited() { animalIdVisited.clear(); }
    public Set<Vector2d> getEquatorEmptyFields() { return equatorEmptyFields; }
    public Set<Vector2d> getNonEquatorEmptyFields() { return nonEquatorEmptyFields; }
}
