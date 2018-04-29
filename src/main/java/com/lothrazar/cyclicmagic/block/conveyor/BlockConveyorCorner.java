package com.lothrazar.cyclicmagic.block.conveyor;

import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockConveyorCorner extends BlockConveyor {

  private BlockConveyor parent;

  public BlockConveyorCorner(SpeedType t, BlockConveyor parent) {
    super(t);
    this.parent = parent;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this, 2), parent, parent);
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    EnumFacing face = getFacingFromState(state).getOpposite();

    UtilEntity.launchDirection(entity, ANGLE, power * 1.5F, face.rotateAround(EnumFacing.Axis.Y));
    //super.onEntityCollidedWithBlock(worldIn, pos, state, entity);
  }
}
