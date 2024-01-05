package model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GenDirectionGenerator implements Iterable<MapDirection>{
    private List<Integer> genotype;
    private int it = 0;

    public GenDirectionGenerator(List<Integer> tab){
        genotype = tab;
    }

    @Override
    public Iterator<MapDirection> iterator() {
        return new Iterator<MapDirection>() {
            @Override
            public boolean hasNext() {
                if (it >= genotype.size()){
                    Collections.reverse(genotype);
                    it = 0;
                }
                return true;
            }

            @Override
            public MapDirection next() {
                if (!hasNext()) return null;
                return MapDirection.toMapDirection(genotype.get(it++));
            }
        };
    }
}
