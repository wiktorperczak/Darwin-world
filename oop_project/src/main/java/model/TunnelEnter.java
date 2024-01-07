package model;

import java.util.Random;

public class TunnelEnter extends WorldElement{
    TunnelExit tunnelExit;

    public TunnelEnter(Vector2d position){
        worldElementType = WorldElementType.TUNNELENTER;
        this.position = position;
    }
    public void setTunnelExit(TunnelExit tunnelExit){
        this.tunnelExit = tunnelExit;
    }

    TunnelExit getTunnelExit(){
        return tunnelExit;
    }

    @Override
    public String toString() {
        return "o";
    }

    @Override
    public String getImagePath() {
        return "/media/dziura_1M.png";
    }
}
