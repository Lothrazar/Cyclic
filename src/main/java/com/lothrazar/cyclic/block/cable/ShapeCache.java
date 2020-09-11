package com.lothrazar.cyclic.block.cable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * At some point either Forge or Mojang made some changes that makes using more than 2 voxel shapes at once explode and die and kill your server completely with no logs or no errors. But it doesnt
 * actually die, it just blocks people from joining. Since apparently they ?cant/wont? fix this, im borrowing the cache idea from RefinedStorage
 * https://github.com/refinedmods/refinedstorage/commit/a9bfe70587fdea0b5c5c253ede4ae3908793a8b6
 */
public class ShapeCache {

  private static final Map<BlockState, VoxelShape> CACHE = new HashMap<>();

  public static VoxelShape getOrCreate(BlockState state, Function<BlockState, VoxelShape> shapeFactory) {
    return CACHE.computeIfAbsent(state, shapeFactory);
  }
}
