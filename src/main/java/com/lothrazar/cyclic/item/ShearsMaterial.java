package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShearsMaterial extends ShearsItem {

  public ShearsMaterial(Properties builder) {
    super(builder);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
    Block block = state.getBlock();
    if (state.isIn(BlockTags.LEAVES) || block != Blocks.COBWEB && block == Blocks.VINE) {
      ItemStack drop = new ItemStack(block);
       worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), drop));
    }
    return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
  }

  @Override
  public boolean canHarvestBlock(BlockState state) {
    Block block = state.getBlock();
    //    BlockTags.?
    //    VineBlock y;
    return block == Blocks.VINE || state.isIn(BlockTags.LEAVES) || // block == Blocks.ACACIA_LEAVES || //TOOD: leaf tag?
        block == Blocks.MELON || block == Blocks.PUMPKIN || super.canHarvestBlock(state);
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
