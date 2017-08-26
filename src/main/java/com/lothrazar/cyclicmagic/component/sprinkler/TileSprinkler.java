package com.lothrazar.cyclicmagic.component.sprinkler;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;

public class TileSprinkler extends TileEntityBaseMachineInvo implements ITickable {
  private static final int RADIUS = 4;
  private static final int TICKS = 30;
  private static final float CHANCE_GROW = 0.4F;
  private static final int TIMER_FULL = 100;
  public TileSprinkler() {
    super(0);
    this.timer = TIMER_FULL;
  }
  @Override
  public boolean isRunning() {
    return (world.getBlockState(this.pos.down()).getBlock() == Blocks.WATER);
  }
  @Override
  public void update() {
    if (this.isInvalid() || !this.isRunning()) { return; }
    //TODO: only run if water underneath and/or nearby. right click message to tell status just like tank
    timer -= 1;
    if (timer > 0) { return; }
    timer = TIMER_FULL;
    BlockPos current;
    for (int x = -1 * RADIUS; x <= RADIUS; x++) {
      for (int z = -1 * RADIUS; z <= RADIUS; z++) {
        if (world.rand.nextDouble() > CHANCE_GROW) {
          continue;
        } //but spawn water, grow or not.different roll for each block
        current = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
        IBlockState bState = world.getBlockState(current);
        Block block = bState.getBlock();
        if (block instanceof IPlantable || block instanceof IGrowable) {
          if (((IGrowable) block).canGrow(world, current, bState, world.isRemote) == false) {
            continue;//its at full growth, stahp
          }
          //plantable has no such
          UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, current);
          //no need to literally increase internal growth numbers, just force more  update ticks
          world.scheduleBlockUpdate(current, block, world.rand.nextInt(TICKS) + 20, 1);
          block.updateTick(world, current, bState, world.rand);
        }
      }
    }
  }
}
