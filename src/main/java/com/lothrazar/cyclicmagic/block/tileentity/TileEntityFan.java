package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFan extends TileEntityBaseMachine implements ITickable {
  private static final int RANGE = 16;
  @Override
  public void update() {
    if (this.isPowered()) {
      EnumFacing facing = getCurrentFacing();
      if (this.getWorld().rand.nextDouble() < 0.01) {//TODO: a regular particle timer instead of rando
        for (int i = 1; i <= RANGE; i++) {
          UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CLOUD, this.getPos().offset(facing, i), 2);
        }
      }
      BlockPos end = this.getCurrentFacingPos().offset(facing, RANGE);
      AxisAlignedBB region = UtilEntity.makeBoundingBoxLine(this.getCurrentFacingPos(), end);
      List<EntityLivingBase> nonPlayer = UtilEntity.getLivingHostile(this.getWorld(), region);
      List<Entity> all = UtilEntity.getItemExp(this.getWorld(), region);
      // center of the block
      double x = this.getPos().getX() + 0.5;
      double y = this.getPos().getY() + 0.7;
      double z = this.getPos().getZ() + 0.5;
//      System.out.println("PUSH THEM "+nonPlayer.size());
//      System.out.println("PUSH stff "+all.size());
//      System.out.println("a"+region.toString());
      UtilEntity.pullEntityList(x, y, z, false, nonPlayer);
      UtilEntity.pullEntityList(x, y, z, false, all);
    }
  }
}
