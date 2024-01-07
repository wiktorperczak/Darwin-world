package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Animal extends WorldElement{
    private MapDirection facingDirection;
    private List<Integer> genotype;
    private Iterator<MapDirection> genDirectionGenerator;


    private int id;
    private int energy;
    private int numberOfDaysLived;
    private int numberOfKids;
    private int numberOfDescendants;
    private List<Animal> kids;
    RectangularMap map;


    public Animal(RectangularMap map, Vector2d position, int id){
        worldElementType = WorldElementType.ANIMAL;
        this.map = map;
        this.position = position;
        genotype = generateGenotype(map.optionsManager.getGenotypeLength());
        //System.out.println(genotype);
        genDirectionGenerator = new GenDirectionGenerator(genotype, map.optionsManager.useReverseGenotype).iterator();
        facingDirection = genDirectionGenerator.next();
        energy = map.optionsManager.getAnimalLife();
        numberOfDaysLived = 0;
        numberOfKids = 0;
        kids = new ArrayList<>();
        this.id = id;
    }

    public void move(RectangularMap map){
        position = calculateNewPosition(map);
        facingDirection = calculateNewRotation();
        energy -= 1;
        numberOfDaysLived += 1;
    }

    Vector2d calculateNewPosition(RectangularMap map){
        if (map.getTunnels().containsKey(position)){
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
        return newPosition;
    }

    MapDirection calculateNewRotation(){
        MapDirection newDirection = genDirectionGenerator.next();
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
    public void setGenotype(List<Integer> newGenotype){ genotype = newGenotype;}
    public int getFacingDirection(){ return facingDirection.toInt();}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public void addChild(Animal child) {
        kids.add(child);
        numberOfKids += 1;
    }

    public Integer visitDescendants(Animal animal) {
        System.out.println("Recursive id: " + animal.getId());
        map.setAnimalIdVisited(this.id, true);
//        System.out.println("Visited");
//        for (boolean elem : map.animalIdVisited) {
//            System.out.print(elem + " ");
//        }
//        System.out.println();
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

    @Override
    public String getImagePath() {
        return "/media/Kot_M.png";
    }

    public void addKid(Animal kid){
        numberOfKids += 1;
        kids.add(kid);
    }

    public List<Animal> getKids() { return kids; }
    public Integer getNumberOfDescendants() { return numberOfDescendants; }
}
