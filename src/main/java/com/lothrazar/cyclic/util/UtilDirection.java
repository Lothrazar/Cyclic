package com.lothrazar.cyclic.util;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.Direction;

public class UtilDirection {

    //determine all permutations of direction order at compile time and cycle through them at runtime
    //the alternative of using a random order is costly and left to chance
    private static List<List<Direction>> permutateDirections(final List<Direction> list, int pos) {
        final List<List<Direction>> results = new ArrayList<>();
        for (int i = pos; i < list.size(); i++) {
            Collections.swap(list, i, pos);
            results.addAll(permutateDirections(list, pos + 1));
            Collections.swap(list, pos, i);
        }
        if (pos == list.size() - 1)
            results.add(new ArrayList<>(list));
        return results;
    }

    public static final Iterator<List<Direction>> inDifferingOrder = Iterables
            .cycle(permutateDirections(Arrays.asList(Direction.values()), 0))
            .iterator();
}
