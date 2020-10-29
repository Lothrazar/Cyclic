package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.BuildShape;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class ShapeCard extends ItemBase {

  private static final String VALID_SHAPE = "cyclic-shape";

  public ShapeCard(Properties properties) {
    super(properties);
  }

  private BuildShape shape;

  public static void setBuildShape(BuildShape shape, ItemStack item) {
    //save
    CompoundNBT tag = item.getOrCreateTag();
    for (BlockPos p : shape.getShape()) {
      // 
      //      p.re
    }
    tag.putBoolean(VALID_SHAPE, true);
  }

  public static BuildShape getBuildShape(ItemStack item) {
    CompoundNBT tag = item.getOrCreateTag();
    if (tag.getBoolean(VALID_SHAPE) == false) {
      return null;
    }
    return null;
  }
}
