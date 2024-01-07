package model;

public class TunnelExit extends WorldElement{
    public TunnelExit(Vector2d position){
        this.position = position;
    }

    @Override
    public String toString() {
        return "x";
    }

    @Override
    public String getImagePath() {
        return "/media/dziura2M.png";
    }
}
