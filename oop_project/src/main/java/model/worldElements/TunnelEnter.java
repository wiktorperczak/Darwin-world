package model.worldElements;

import model.map.utils.Vector2d;

public class TunnelEnter extends WorldElement {
    private TunnelEnter tunnelExit;

    public TunnelEnter(Vector2d position){
        worldElementType = WorldElementType.TUNNEL;
        this.position = position;
    }
    public void setTunnelExit(TunnelEnter tunnelExit){
        this.tunnelExit = tunnelExit;
    }

    public TunnelEnter getTunnelExit(){
        return tunnelExit;
    }

    @Override
    public String toString() {
        return "o";
    }

}
