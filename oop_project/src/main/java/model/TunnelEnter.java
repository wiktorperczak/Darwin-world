package model;

import java.util.Random;

public class TunnelEnter extends WorldElement{
    TunnelEnter tunnelExit;

    public TunnelEnter(Vector2d position){
        worldElementType = WorldElementType.TUNNEL;
        this.position = position;
    }
    public void setTunnelExit(TunnelEnter tunnelExit){
        this.tunnelExit = tunnelExit;
    }

    TunnelEnter getTunnelExit(){
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
