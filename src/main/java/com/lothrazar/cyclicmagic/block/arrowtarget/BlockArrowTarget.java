package com.lothrazar.cyclicmagic.block.arrowtarget;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.block.BlockLever;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockArrowTarget extends BlockBaseHasTile implements IHasRecipe {

  public static final PropertyBool POWERED = BlockLever.POWERED;//PropertyBool.create("powered");


  public BlockArrowTarget() {
    super(Material.ROCK);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityArrowTarget();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, POWERED);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, false);
  }

  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(POWERED) ? 1 : 0);
  }

  private int getPower(IBlockAccess world, BlockPos pos, EnumFacing side) {
    if (world.getTileEntity(pos) instanceof TileEntityArrowTarget) {
      TileEntityArrowTarget target = ((TileEntityArrowTarget) world.getTileEntity(pos));
      return target.getPower();
    }
    return 0;
  }

  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }

  @Override
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        "iii",
        "sos",
        "iii",
        'i', "nuggetIron",
        'o', Blocks.OBSERVER,
        's', Blocks.STONE_BUTTON);
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    super.onEntityCollidedWithBlock(world, pos, state, entity);
    //ignore vertical sides.
    double x = entity.posX - pos.getX();
    double z = entity.posZ - pos.getZ();
    double xDist = x - 0.5;
    double zDist = z - 0.5;
    if (!world.isRemote && Math.abs(zDist) < 1 && Math.abs(xDist) < 1) {// distanceToCenter < 1
      //DETECT which horizontal side
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
      double distanceToCenter = Math.sqrt(horizDistance * horizDistance + vertDistance * vertDistance);
      int redstone = Const.REDSTONE_MAX - Math.min((int) (distanceToCenter * 26), Const.REDSTONE_MAX);
      if (world.getTileEntity(pos) instanceof TileEntityArrowTarget) {
        TileEntityArrowTarget target = ((TileEntityArrowTarget) world.getTileEntity(pos));
        target.setPower(redstone);
      }
    }
  }
}
