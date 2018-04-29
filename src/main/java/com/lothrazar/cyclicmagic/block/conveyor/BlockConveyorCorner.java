package com.lothrazar.cyclicmagic.block.conveyor;

import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockConveyorCorner extends BlockConveyor {

  public static final PropertyBool FLIPPED = PropertyBool.create("flipped");
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
    EnumFacing face = getFacingFromState(state);
    if (state.getValue(FLIPPED)) {
      face = face.getOpposite();
    }
    //are we going ahead or turning the corner
    boolean forward = false;//worldIn.rand.nextDouble() > 0.5;
    if (forward) {//first part: go ahead
      tickMovement(pos, entity, face);
    }
    else {// turn the corner
      tickMovement(pos, entity, face.rotateAround(EnumFacing.Axis.Y));
    }
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    //use HIT xyz to fix facing direction as well?!?!?
    IBlockState here = worldIn.getBlockState(pos);

      worldIn.setBlockState(pos, here.cycleProperty(FLIPPED));

    return true;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int meta = (state.getValue(FLIPPED) ? 10 : 0);
    EnumFacing facing = state.getValue(PROPERTYFACING);
    int facingbits = facing.getHorizontalIndex();
    return facingbits + meta;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { PROPERTYFACING, FLIPPED });
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    boolean flipped = false;
    if (meta >= 10) {
      flipped = true;
      meta -= 10;
    }
    return super.getStateFromMeta(meta).withProperty(FLIPPED, flipped);
  }
}
