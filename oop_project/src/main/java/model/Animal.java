package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Animal extends WorldElement{
    private MapDirection facingDirection;
    private List<Integer> genotype;
    private Iterator<MapDirection> genDirectionGenerator;
    private int life;


    public Animal(Vector2d position){
        OptionsManager optionsManager = OptionsManager.getInstance();
        this.position = position;
        genotype = generateGenotype(optionsManager.getGenotypeLength());
        //System.out.println(genotype);
        genDirectionGenerator = new GenDirectionGenerator(genotype).iterator();
        facingDirection = genDirectionGenerator.next();
        life = optionsManager.getAnimalLife();
    }

    public void move(RectangularMap map){
        position = calculateNewPosition(map);
        facingDirection = calculateNewRotation();
        life -= 1;
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
        return life > 0;
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
        };
    }
}
