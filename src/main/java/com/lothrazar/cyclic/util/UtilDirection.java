package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.Direction;

public class UtilDirection {

  //determine all permutations of direction order at compile time and cycle through them at runtime
  //the alternative of using a random order is costly and left to chance
  public static List<List<Direction>> permutateDirections(final List<Direction> list, int pos) {
    final List<List<Direction>> results = new ArrayList<>();
    for (int i = pos; i < list.size(); i++) {
      Collections.swap(list, i, pos);
      results.addAll(permutateDirections(list, pos + 1));
      Collections.swap(list, pos, i);
    }
    if (pos == list.size() - 1) {
      results.add(new ArrayList<>(list));
    }
    return results;
  }
}
