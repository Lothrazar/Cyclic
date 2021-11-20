package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilBlockstates;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FireplaceBlock extends BlockBase {

  public FireplaceBlock(Properties properties) {
    super(properties.strength(1.8F));
    this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    boolean isPowered = worldIn.hasNeighborSignal(pos);
    BlockPos posFire = pos.relative(state.getValue(BlockStateProperties.FACING));
    if (worldIn.getBlockState(posFire).canOcclude()) {
      posFire = posFire.above(); //if i am facing a block, light its top side.
      //use case: facing obsidian or wood plank instead of air
    }
    if (isPowered && !state.getValue(LIT)) { //set fire
      if (setFire(worldIn, posFire, false) && worldIn.isClientSide) {
        UtilSound.playSound(worldIn, pos, SoundEvents.FLINTANDSTEEL_USE);
      }
    }
    else if (!isPowered && state.getValue(LIT)) { //put out fire
      if (setFire(worldIn, posFire, true)) {
        //extinguish sound sent using playEvent
        //which ends up in WorldRenderer line 2200 ish
        worldIn.levelEvent(1009, pos, 0);
      }
    }
    worldIn.setBlockAndUpdate(pos, state.setValue(LIT, isPowered));
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
  }

  private boolean setFire(Level worldIn, BlockPos pos, boolean extinguish) {
    BlockState state = worldIn.getBlockState(pos);
    if ((state.getBlock() == Blocks.CAMPFIRE
        || state.getBlock() == Blocks.SOUL_CAMPFIRE)
        && extinguish == state.getValue(CampfireBlock.LIT)) {
      //set lit since its not lit
      return worldIn.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, !extinguish));
    }
    if (extinguish && canExtinguish(worldIn, pos)) {
      return worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
    else if (extinguish && canExtinguishInfiniteFire(worldIn, pos)) {
      return worldIn.setBlockAndUpdate(pos.relative(Direction.UP), Blocks.AIR.defaultBlockState());
    }
    else if ((!extinguish && canSetFire(worldIn, pos))) {
      return worldIn.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
    }
    else if (!extinguish && canSetInfiniteFire(worldIn, pos)) {
      return worldIn.setBlockAndUpdate(pos.relative(Direction.UP), Blocks.FIRE.defaultBlockState());
    }
    return false;
  }

  private boolean canSetFire(Level worldIn, BlockPos pos) {
    return worldIn.isEmptyBlock(pos) && worldIn.getBlockState(pos.relative(Direction.DOWN)).canOcclude();
  }

  private boolean canSetInfiniteFire(Level worldIn, BlockPos pos) {
    return worldIn.isEmptyBlock(pos.relative(Direction.UP)) && hasInfiniburnTag(worldIn, pos);
  }

  private boolean canExtinguish(Level worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos).getBlock() == Blocks.FIRE;
  }

  private boolean canExtinguishInfiniteFire(Level worldIn, BlockPos pos) {
    return hasInfiniburnTag(worldIn, pos) && canExtinguish(worldIn, pos.relative(Direction.UP));
  }

  private boolean hasInfiniburnTag(Level worldIn, BlockPos pos) {
    return (BlockTags.INFINIBURN_END.contains(worldIn.getBlockState(pos).getBlock()) && worldIn.dimension() == Level.END)
        || (BlockTags.INFINIBURN_NETHER.contains(worldIn.getBlockState(pos).getBlock()) && worldIn.dimension() == Level.NETHER)
        || (BlockTags.INFINIBURN_OVERWORLD.contains(worldIn.getBlockState(pos).getBlock()) && worldIn.dimension() == Level.OVERWORLD);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlock(pos, state.setValue(BlockStateProperties.FACING, UtilBlockstates.getFacingFromEntity(pos, entity)), 2);
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(LIT);
  }
}
