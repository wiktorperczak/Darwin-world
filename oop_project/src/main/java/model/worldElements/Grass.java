package model.worldElements;

import model.map.utils.Vector2d;

public class Grass extends WorldElement {
    public Grass(Vector2d position){
        worldElementType = WorldElementType.GRASS;
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
