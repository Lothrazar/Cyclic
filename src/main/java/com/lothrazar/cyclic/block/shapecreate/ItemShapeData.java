package com.lothrazar.cyclic.block.shapecreate;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.item.ItemStack;

public class ItemShapeData extends ItemBase {

  public ItemShapeData(Properties properties) {
    super(properties);
  }

  public void setShape(ItemStack stack, ShapeData shape) {
    shape.write(stack.getOrCreateTag());
  }
}
