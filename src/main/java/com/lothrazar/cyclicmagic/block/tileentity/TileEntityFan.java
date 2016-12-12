package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFan extends TileEntityBaseMachine implements ITickable {
  private static final int RANGE = 16;
  private static final float SPEED = 0.13F;
  @Override
  public void update() {
    if (this.isPowered()) {
      EnumFacing facing = getCurrentFacing();
      if (this.getWorld().rand.nextDouble() < 0.001) {//TODO: a regular particle timer instead of rando
        for (int i = 1; i <= RANGE; i++) {
          UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CLOUD, this.getPos().offset(facing, i), 2);
        }
      }
      BlockPos end = this.getCurrentFacingPos().offset(facing, RANGE).up();

      BlockPos start = this.getPos();
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
      List<EntityLivingBase> nonPlayer = this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, region);//UtilEntity.getLivingHostile(, region);
      // center of the block
      double x = this.getPos().getX() + 0.5;
      double y = this.getPos().getY() + 0.7;
      double z = this.getPos().getZ() + 0.5;
    
      UtilEntity.pullEntityList(x, y, z, false, nonPlayer,SPEED,SPEED);
    }
  }
}
