package model;

public class Grass extends WorldElement{
    public Grass(Vector2d position){
        worldElementType = WorldElementType.GRASS;
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
