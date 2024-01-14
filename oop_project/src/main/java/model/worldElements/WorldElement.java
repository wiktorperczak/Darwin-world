package model.worldElements;

import model.map.utils.Vector2d;

public abstract class WorldElement {
    protected Vector2d position;
    public WorldElementType worldElementType;
    public Vector2d getPosition(){
        return position;
    }
}
