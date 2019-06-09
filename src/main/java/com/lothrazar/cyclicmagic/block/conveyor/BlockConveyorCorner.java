package com.lothrazar.cyclicmagic.block.conveyor;

import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockConveyorCorner extends BlockConveyor {

  public static final PropertyBool FLIPPED = PropertyBool.create("flipped");

  public BlockConveyorCorner(SpeedType t) {
    super(t);
    setCorner(this);
  }

  @Override
  public boolean isCorner() {
    return true;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 2),
        "cc",
        "uu",
        'c', flatType,
        'u', new ItemStack(Blocks.COBBLESTONE));
  }

  @Override
  public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
      return;
    }
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

  /**
   * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the IBlockstate
   */
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    //flip on sneak
    return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
        .withProperty(FLIPPED, placer.isSneaking());
  }

  public void setDrop(BlockConveyor drop) {
    this.flatType = drop;
  }
}
