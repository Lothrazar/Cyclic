package com.lothrazar.cyclic.block;

import javax.annotation.Nullable;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireplaceBlock extends BlockBase {

  public FireplaceBlock(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setDefaultState(this.getDefaultState().with(LIT, false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    boolean isPowered = worldIn.isBlockPowered(pos);
    BlockPos posFire = pos.offset(state.get(BlockStateProperties.FACING));
    if (worldIn.getBlockState(posFire).isSolid()) {
      posFire = posFire.up();//if i am facing a block, light its top side.
      //use case: facing obsidian or wood plank instead of air
    }
    if (isPowered && !state.get(LIT)) { //set fire
      if (setFire(worldIn, posFire, false)) {
        UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), pos, SoundEvents.ITEM_FLINTANDSTEEL_USE);
      }
    }
    else if (!isPowered && state.get(LIT)) { //put out fire
      if (setFire(worldIn, posFire, true)) {
        //extinguish sound sent using playEvent
        //which ends up in WorldRenderer line 2200 ish
        worldIn.playEvent(1009, pos, 0);
      }
    }
    worldIn.setBlockState(pos, state.with(LIT, isPowered));
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
  }

  private boolean setFire(World worldIn, BlockPos pos, boolean extinguish) {
    if (extinguish && canExtinguish(worldIn, pos)) {
      worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
      return true;
    }
    else if (extinguish && canExtinguishInfiniteFire(worldIn, pos)) {
      worldIn.setBlockState(pos.offset(Direction.UP), Blocks.AIR.getDefaultState());
      return true;
    }
    else if ((!extinguish && canSetFire(worldIn, pos))) {
      worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState());
      return true;
    }
    else if (!extinguish && canSetInfiniteFire(worldIn, pos)) {
      worldIn.setBlockState(pos.offset(Direction.UP), Blocks.FIRE.getDefaultState());
      return true;
    }
    return false;
  }

  private boolean canSetFire(World worldIn, BlockPos pos) {
    return worldIn.isAirBlock(pos) && worldIn.getBlockState(pos.offset(Direction.DOWN)).isSolid();
  }

  private boolean canSetInfiniteFire(World worldIn, BlockPos pos) {
    return worldIn.isAirBlock(pos.offset(Direction.UP))
        && hasInfiniburnTag(worldIn, pos);
  }

  private boolean canExtinguish(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos).getBlock() == Blocks.FIRE;
  }

  private boolean canExtinguishInfiniteFire(World worldIn, BlockPos pos) {
    return hasInfiniburnTag(worldIn, pos) && canExtinguish(worldIn, pos.offset(Direction.UP));
  }

  private boolean hasInfiniburnTag(World worldIn, BlockPos pos) {
    return (BlockTags.INFINIBURN_END.contains(worldIn.getBlockState(pos).getBlock()) && worldIn.getDimensionKey() == World.THE_END)
        || (BlockTags.INFINIBURN_NETHER.contains(worldIn.getBlockState(pos).getBlock()) && worldIn.getDimensionKey() == World.THE_NETHER)
        || (BlockTags.INFINIBURN_OVERWORLD.contains(worldIn.getBlockState(pos).getBlock()) && worldIn.getDimensionKey() == World.OVERWORLD);
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
