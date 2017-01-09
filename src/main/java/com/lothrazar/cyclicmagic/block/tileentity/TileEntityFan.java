package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFan extends TileEntityBaseMachine implements ITickable {
  private static final int MAX_RANGE = 16;
  private static final float SPEED = 0.13F;
  private static final int TIMER_FULL = 80;
  private static final String NBT_TIMER = "Timer";
  private int timer;
  @Override
  public void update() {
    if (this.isPowered()) {
      EnumFacing facing = getCurrentFacing();
      int rangeFixed = getCurrentRange(); //can go up to max range unless hits a solid
      // System.out.println("rangeFixed"+rangeFixed);
      if (this.timer == 0) {
        this.timer = TIMER_FULL;
        //rm this its ugly, keep in case i add a custom particle
        doParticles(rangeFixed);
      }
      else {
        this.timer--;
      }
      pushEntities(facing, rangeFixed);//int pushed = 
    }
    else {
      this.timer = 0;
    }
  }
  private int pushEntities(EnumFacing facing, int rangeFixed) {
    BlockPos start = this.getPos();
    BlockPos end = this.getCurrentFacingPos().offset(facing, rangeFixed).up();//.up()
    //without this hotfix, fan works only on the flatedge of the band, not the 1x1 area
    switch (facing.getAxis()) {
      case X:
        end = end.add(0, 0, 1);
      break;
      case Y:
      break;
      case Z:
        end = end.add(1, 0, 0);
      break;
      default:
      break;
    }
    AxisAlignedBB region = new AxisAlignedBB(start, end);
    List<Entity> nonPlayer = this.getWorld().getEntitiesWithinAABB(Entity.class, region);//UtilEntity.getLivingHostile(, region);
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 0.7;
    double z = this.getPos().getZ() + 0.5;
    UtilEntity.pullEntityList(x, y, z, false, nonPlayer, SPEED, SPEED);
    return nonPlayer.size();
  }
  private void doParticles(int rangeFixed) {
    EnumFacing facing = getCurrentFacing();
    for (int i = 1; i <= rangeFixed; i++) {
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CLOUD, this.getPos().offset(facing, i), 1);
    }
  }
  private int getCurrentRange() {
    EnumFacing facing = getCurrentFacing();
    BlockPos tester;
    for (int i = 1; i <= MAX_RANGE; i++) {//if we start at fan, we hit MYSELF (the fan)
      tester = this.getPos().offset(facing, i);
      if (canBlowThrough(tester) == false) {
        //cant pass thru
        //System.out.println("Cant pass thru"+tester.toString() +this.getWorld().getBlockState(tester).getBlock().toString());
        return i;
      }
    }
    return MAX_RANGE;
  }
  private boolean canBlowThrough(BlockPos tester) {
    //passes through air, and anything NOT a full block
    return this.getWorld().isAirBlock(tester) || !this.getWorld().getBlockState(tester).isFullBlock();
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
}
