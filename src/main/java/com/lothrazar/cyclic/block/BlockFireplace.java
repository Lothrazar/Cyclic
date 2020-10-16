package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFireplace extends BlockBase {
  public BlockFireplace(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setDefaultState(this.getDefaultState().with(LIT, false));
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    if (worldIn.isRemote)
      return;
    boolean isPowered = worldIn.isBlockPowered(pos);
    BlockPos posFire = pos.offset(state.get(BlockStateProperties.FACING));
    BlockPos posBase = posFire.offset(Direction.DOWN);
    if (isPowered && !state.get(LIT)) { //set fire
      if (worldIn.isAirBlock(posFire) && worldIn.getBlockState(posBase).isSolid()) {
        worldIn.setBlockState(posFire, Blocks.FIRE.getDefaultState());
        UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), pos, SoundEvents.ITEM_FLINTANDSTEEL_USE);
      }
      else
        UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), pos, SoundEvents.BLOCK_LEVER_CLICK);
    }
    else if (!isPowered && state.get(LIT)) { //put out fire
      if (worldIn.getBlockState(posFire).getBlock() == Blocks.FIRE) {
        worldIn.setBlockState(posFire, Blocks.AIR.getDefaultState());
        UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
      }
      else
        UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), pos, SoundEvents.BLOCK_LEVER_CLICK);
    }
    worldIn.setBlockState(pos, state.with(LIT, isPowered));
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.FACING, UtilStuff.getFacingFromEntity(pos, entity)), 2);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(LIT);
  }
}
