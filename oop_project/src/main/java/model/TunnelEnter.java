package model;

import java.util.Random;

public class TunnelEnter extends WorldElement{
    TunnelExit tunnelExit;

    public TunnelEnter(Vector2d position){
        this.position = position;
        tunnelExit = new TunnelExit(getRandomPos());
    }

    Vector2d getRandomPos(){
        Random random = new Random();
        return new Vector2d(random.nextInt(3), random.nextInt(3));
    }

    TunnelExit getTunnelExit(){
        return tunnelExit;
    }

    @Override
    public String toString() {
        return "o";
    }
}
