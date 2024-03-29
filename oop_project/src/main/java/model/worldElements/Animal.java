package model.worldElements;

import model.map.RectangularMap;
import model.map.utils.GenDirectionGenerator;
import model.map.utils.MapDirection;
import model.map.utils.Vector2d;

import java.util.*;

public class Animal extends WorldElement {
    private MapDirection facingDirection;
    private List<Integer> genotype;
    private GenDirectionGenerator genDirectionGenerator;
    private Iterator<MapDirection> genIterator;
    private List<Animal> kids;
    private RectangularMap map;


    private boolean isTunnelUsedLastMove;
    private int id;
    private int energy;
    private int numberOfDaysLived;
    private int numberOfKids;
    private int numberOfDescendants;
    private int grassEaten;
    private int dayOfDeath;


    public Animal(RectangularMap map, Vector2d position, int id){
        worldElementType = WorldElementType.ANIMAL;
        this.map = map;
        this.position = position;
        genotype = generateGenotype(map.optionsManager.getGenotypeLength());
        genDirectionGenerator = new GenDirectionGenerator(genotype, map.optionsManager.getUseReverseGenotype());
        genIterator = genDirectionGenerator.iterator();
        facingDirection = genIterator.next();
        energy = map.optionsManager.getAnimalLife();
        numberOfDaysLived = 0;
        numberOfKids = 0;
        grassEaten = 0;
        dayOfDeath = -1;
        kids = new ArrayList<>();
        isTunnelUsedLastMove = false;
        this.id = id;
    }

    public void move(RectangularMap map){
        position = calculateNewPosition(map);
        facingDirection = calculateNewRotation();
        energy -= 1;
        numberOfDaysLived += 1;
    }

    Vector2d calculateNewPosition(RectangularMap map){
        if (!isTunnelUsedLastMove && map.getTunnels().containsKey(position)){
            isTunnelUsedLastMove = true;
            return map.getTunnels().get(position).getTunnelExit().getPosition();
        }
        Vector2d newPosition = position.add(facingDirection.toUnitVector());
        Vector2d boundaries = map.getBoundaries();
        if (newPosition.getX() > boundaries.getX()){
            newPosition = newPosition.add(new Vector2d(-boundaries.getX() - 1, 0));
        } else if (newPosition.getX() < 0){
            newPosition = newPosition.add(new Vector2d(boundaries.getX() + 1, 0));
        }

        if (newPosition.getY() > boundaries.getY() || newPosition.getY() < 0){
            newPosition = new Vector2d(newPosition.getX(), position.getY());
            facingDirection = facingDirection.rotate(4);
        }
        isTunnelUsedLastMove = false;
        return newPosition;
    }

    MapDirection calculateNewRotation(){
        MapDirection newDirection = genIterator.next();
        return facingDirection.rotate(newDirection.toInt());
    }

    List<Integer> generateGenotype(int length){
        List<Integer> genList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < length; i++){
            genList.add(rand.nextInt(8));
        }
        return genList;
    }

    public boolean isAlive(){
        return energy > 0;
    }

    public Integer visitDescendants(Animal animal) {
        map.setAnimalIdVisited(animal.getId(), true);

        int res = 0;
        for (Animal kid : animal.getKids()) {
            if (!map.getAnimalIdVisited(kid.getId())) {
                res += 1 + visitDescendants(kid);
            }
        }
        return res;
    }

    public void calculateNumberOfDescendants() {
        map.resetAnimalIdVisited();
        numberOfDescendants = visitDescendants(this);
    }

    public void addKid(Animal kid){
        numberOfKids += 1;
        kids.add(kid);
    }

    public void randomizeGenotypeIterator(){
        Random random = new Random();
        for (int i = 0; i < random.nextInt(map.optionsManager.getGenotypeLength()); i++){
            genIterator.next();
        }
    }

    public void randomizeStartingRotation() {
        Random random = new Random();
        facingDirection = MapDirection.toMapDirection(random.nextInt(8));
    }

    public Integer getActiveGen() {
        genIterator.hasNext();
        return genDirectionGenerator.getActiveGen();
    }

    public String toString() {
        return switch (facingDirection){
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        } + getEnergy();
    }

    public int getEnergy(){ return energy;}
    public void addEnergy(int value){ energy += value;}
    public void setEnergy(int value){ energy = value;}
    public int getNumberOfDaysLived(){ return numberOfDaysLived;}
    public int getNumberOfKids(){ return numberOfKids;}
    public List<Integer> getGenotype(){return genotype;}
    public void setGenotype(List<Integer> newGenotype){
        genotype = new ArrayList<>(newGenotype);
        genDirectionGenerator = new GenDirectionGenerator(genotype, map.optionsManager.getUseReverseGenotype());
        genIterator = genDirectionGenerator.iterator();
    }
    public int getFacingDirection(){ return facingDirection.toInt();}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public List<Animal> getKids() { return kids; }
    public Integer getNumberOfDescendants() { return numberOfDescendants; }
    public void addGrassEaten(){
        grassEaten += 1;
    }
    public int getGrassEaten(){
        return grassEaten;
    }
    public Integer getDayOfDeath() {
        if (dayOfDeath == -1) return map.getDaysSimulated();
        return dayOfDeath;
    }
    public void setDayOfDeath(int daysSimulated) {
        dayOfDeath = daysSimulated;
    }

    public boolean genotypeHasGen(int gen){
        return genotype.contains(gen);
    }

    public void setFacingDirection(MapDirection direction){
        facingDirection = direction;
    }

    public void setNumberOfKids(int numberOfKids){
        this.numberOfKids = numberOfKids;
    }

    public void setNumberOfDaysLived(int numberOfDaysLived){
        this.numberOfDaysLived = numberOfDaysLived;
    }
}
