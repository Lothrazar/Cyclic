package com.lothrazar.cyclic.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;

public class ItemObsShears extends ShearsItem {

  public ItemObsShears(Properties builder) {
    super(builder);
  }

  @Override
  public boolean canHarvestBlock(BlockState blockIn) {
    Block block = blockIn.getBlock();
    return block == Blocks.MELON || block == Blocks.PUMPKIN || super.canHarvestBlock(blockIn);
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Block block = state.getBlock();
    if (block == Blocks.MELON || block == Blocks.PUMPKIN) {
      return 15F;
    }
    return super.getDestroySpeed(stack, state);
  }
}
