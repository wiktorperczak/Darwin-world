package model;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public String toString(){
        return switch (this){
            case NORTH -> "north";
            case NORTH_EAST -> "north-east";
            case EAST -> "east";
            case SOUTH_EAST -> "south-east";
            case SOUTH -> "south";
            case SOUTH_WEST -> "south-west";
            case WEST -> "west";
            case NORTH_WEST -> "north-west";
        };
    }

    public int toInt(){
        return switch (this){
            case NORTH -> 0;
            case NORTH_EAST -> 1;
            case EAST -> 2;
            case SOUTH_EAST -> 3;
            case SOUTH -> 4;
            case SOUTH_WEST -> 5;
            case WEST -> 6;
            case NORTH_WEST -> 7;
        };
    }

    public static MapDirection toMapDirection(int i){
        i %= 8;
        return switch (i){
            case 0 -> NORTH;
            case 1 -> NORTH_EAST;
            case 2 -> EAST;
            case 3 -> SOUTH_EAST;
            case 4 -> SOUTH;
            case 5 -> SOUTH_WEST;
            case 6 -> WEST;
            case 7 -> NORTH_WEST;
            default -> null;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }

    public MapDirection rotate(int i){
        int base = this.toInt() + i;
        return toMapDirection(base);
    }
}
