package com.lothrazar.cyclicmagic.util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class UtilPlantable {
  public static ItemStack tryPlantSeed(World world, BlockPos posForPlant, ItemStack stack) {
    BlockPos posSoil = posForPlant.down();
    if (stack != null && stack.getItem() instanceof IPlantable) {
      IPlantable seed = (IPlantable) stack.getItem();
      IBlockState crop = seed.getPlant(world, posForPlant);
      if (crop != null) {
        // mimic exactly what onItemUse.onItemUse is doing
        IBlockState state = world.getBlockState(posSoil);
        boolean canSustainPlant = state.getBlock().canSustainPlant(state, world, posSoil, EnumFacing.UP, seed);
        if (canSustainPlant) {
          if (world.isAirBlock(posForPlant)) {
            world.setBlockState(posForPlant, crop);
            stack.stackSize--;
            return stack;
          }
          else {
            // System.out.println("can sustain plant yes, but not an air block.
            // so do NOT drop on ground");
            return stack;// ie, dont do super
          }
        }
      }
    }
    return null;
  }
}
