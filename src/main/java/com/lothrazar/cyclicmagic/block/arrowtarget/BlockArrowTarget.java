package com.lothrazar.cyclicmagic.block.arrowtarget;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockArrowTarget extends BlockBase implements IHasRecipe {

  public BlockArrowTarget() {
    super(Material.ROCK);
  }

  @Override
  public IRecipe addRecipe() {
    return null;
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    super.onEntityCollidedWithBlock(world, pos, state, entity);
    //ignore vertical sides.
    //DETECT horizontal side
    double x = entity.posX - pos.getX();
    //    double y = entity.posY - pos.getY();
    double z = entity.posZ - pos.getZ();
    double xDist = x - 0.5;
    double zDist = z - 0.5;
    // ModCyclic.logger.log(entity.posX + "," + entity.posY + "," + entity.posZ);
    if (!world.isRemote && Math.abs(zDist) < 1 && Math.abs(xDist) < 1) {// distanceToCenter < 1
      //      ModCyclic.logger.log("entity.motionZ  " + entity.motionZ);
      //      ModCyclic.logger.log("isCollidedVertically: " + entity.isCollidedVertically);
      //      ModCyclic.logger.log("onGround: " + entity.onGround);
      //(y - 0.5) * (y - 0.5) +
      EnumFacing side = null;
      if (x > 0.99) {
        side = EnumFacing.EAST;
      }
      else if (x < 0) {
        side = EnumFacing.WEST;
      }
      else if (z > 0.99) {
        side = EnumFacing.SOUTH;
      }
      else if (z < 0) {
        side = EnumFacing.NORTH;
      }
      double horizDistance = 0;
      if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
        horizDistance = xDist;
      }
      else {
        horizDistance = zDist;
      }
      double y = entity.posY - pos.getY();
      double vertDistance = y - 0.5;
      ModCyclic.logger.log("horizDistance  " + horizDistance);
      ModCyclic.logger.log("vertDistance  " + vertDistance);
      //IF NORTH AND SOUTH
      //x is horixontal
      //      xDist = x - 0.5;
      //      zDist = z - 0.5;
      double distanceToCenter = Math.sqrt(horizDistance * horizDistance + vertDistance * vertDistance);
      //" relative xz : " + x + " , " + z + 
      ModCyclic.logger.log(side + " :::" + world.isRemote + " distance " + distanceToCenter);
    }
  }
}
