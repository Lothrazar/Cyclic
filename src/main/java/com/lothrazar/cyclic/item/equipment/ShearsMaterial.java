package com.lothrazar.cyclic.item.equipment;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tags.BlockTags;

public class ShearsMaterial extends ShearsItem {

  public ShearsMaterial(Properties builder) {
    super(builder);
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Block block = state.getBlock();
    if (block == Blocks.MELON
        || block == Blocks.PUMPKIN
        || block.isIn(BlockTags.CARPETS)
        || block.isIn(BlockTags.WOOL)
        || state.isIn(BlockTags.LEAVES)) {
      return 15F;
    }
    return super.getDestroySpeed(stack, state);
  }
}
