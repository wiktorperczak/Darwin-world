package model;

public class Grass extends WorldElement{
    public Grass(Vector2d position){
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
