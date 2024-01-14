package model.map.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GenDirectionGenerator implements Iterable<MapDirection>{
    private List<Integer> genotype;

    private int it = 0;
    private final boolean useReverseGenotype;

    public GenDirectionGenerator(List<Integer> tab, boolean useReverseGenotype){

        genotype = new ArrayList<>(tab);
        this.useReverseGenotype = useReverseGenotype;
    }

    @Override
    public Iterator<MapDirection> iterator() {
        return new Iterator<MapDirection>() {
            @Override
            public boolean hasNext() {
                if (it >= genotype.size()){
                    if (useReverseGenotype) { Collections.reverse(genotype); }
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

    public Integer getActiveGen() {
        return genotype.get(it);
    }
}
