package com.lothrazar.cyclicmagic.component.sprinkler;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;

public class TileSprinkler extends TileEntityBaseMachineInvo implements ITickable {
  private static final int RADIUS = 4;
  private static final int TICKS = 30;
  private static final float CHANCE_GROW = 0.4F;
  private static final int TIMER_FULL = 100;
  private boolean spawnParticles = true;
  private int timerUserToggle;
  public TileSprinkler() {
    super(0);
    this.timer = TIMER_FULL;
    this.timerUserToggle = 0;
  }
  @Override
  public boolean isRunning() {
    return (world.getBlockState(this.pos.down()).getBlock() == Blocks.WATER);
  }
  @Override
  public void update() {
    if (this.isInvalid() || !this.isRunning()) {
      return;
    }
    if (timerUserToggle > 0)
      timerUserToggle--;
    //TODO: only run if water underneath and/or nearby. right click message to tell status just like tank
    timer -= 1;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL;
    BlockPos current;
    for (int x = -1 * RADIUS; x <= RADIUS; x++) {
      for (int z = -1 * RADIUS; z <= RADIUS; z++) {
        if (world.rand.nextDouble() > CHANCE_GROW) {
          continue;
        } //but spawn water, grow or not.different roll for each block
        current = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
        IBlockState bState = world.getBlockState(current);
        if (bState == null || bState.getBlock() == null) {
          continue;
        }
        Block block = bState.getBlock();
        if (block instanceof IPlantable || block instanceof IGrowable) {
          if (block instanceof IGrowable &&
              ((IGrowable) block).canGrow(world, current, bState, world.isRemote) == false) {
            continue;//its at full growth, stahp
          }
          //plantable has no such
          if (spawnParticles && world.isRemote) {
            UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, current);
          }
          try {//no need to literally increase internal growth numbers, just force more  update ticks
            world.scheduleBlockUpdate(current, block, world.rand.nextInt(TICKS) + 20, 1);
            block.updateTick(world, current, bState, world.rand);
          }
          catch (Exception e) {
            ModCyclic.logger.error("Sprinkler by Cyclic has encountered an error while growing a plant, contact both mod authors    " + block);
            ModCyclic.logger.error(e.getMessage());
            e.printStackTrace();
          }
        }
      }
    }
  }
  public void toggleSpawnParticles() {
    if (timerUserToggle > 0) {
      return;
    }
    this.spawnParticles = !this.spawnParticles;
    timerUserToggle = 15;
  }
  public boolean isSpawningParticles() {
    return this.spawnParticles;
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    spawnParticles = compound.getBoolean("spawnParticles");
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setBoolean("spawnParticles", spawnParticles);
    return super.writeToNBT(compound);
  }
}
