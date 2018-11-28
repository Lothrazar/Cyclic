package com.lothrazar.cyclicmagic.item;

import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IRenderOutline {

  /**
   * exact array of size three for RGB colour codes
   * 
   * @return
   */
  public int[] getRgb();

  /**
   * any item can render outlines. See EventRender.class
   * 
   * @param world
   * @param heldItem
   * @param mouseOver
   * @return
   */
  public Set<BlockPos> renderOutline(World world, ItemStack heldItem, RayTraceResult mouseOver);
}
