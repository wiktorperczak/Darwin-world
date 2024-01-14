package model;

public abstract class WorldElement {
    protected Vector2d position;
    public WorldElementType worldElementType;

    public Vector2d getPosition(){
        return position;
    }
}
