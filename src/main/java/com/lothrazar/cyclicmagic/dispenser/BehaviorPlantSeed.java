package com.lothrazar.cyclicmagic.dispenser;
import com.lothrazar.cyclicmagic.util.UtilPlantable;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorPlantSeed extends BehaviorDefaultDispenseItem {
  @Override
  public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    // we want to place in front of the dispenser 
    //which is based on where its facing
   
    //changed in 1.10
    BlockPos posForPlant = UtilWorld.convertIposToBlockpos(BlockDispenser.getDispensePosition(source));
        //source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
    
    ItemStack returning = UtilPlantable.tryPlantSeed(world, posForPlant, stack);
    if (returning == null)
      return super.dispenseStack(source, stack);
    else
      return returning;
  }
}
