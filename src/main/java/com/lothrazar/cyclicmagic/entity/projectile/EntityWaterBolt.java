package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWaterBolt extends EntityThrowable {
  public static Item renderSnowball;
  public EntityWaterBolt(World worldIn) {
    super(worldIn);
  }
  public EntityWaterBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityWaterBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public static final int nether = -1;
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      if (mop.entityHit instanceof EntityLivingBase) {
        EntityLivingBase e = (EntityLivingBase) mop.entityHit;
        if (e.isBurning()) {
          e.extinguish();
        }
      }
    }
    BlockPos pos = mop.getBlockPos();
    if (pos == null) {
      pos = this.getPosition();
    }
    World world = getEntityWorld();
    if (pos != null) {
      // UtilParticle.spawnParticle(this.worldObj,
      // EnumParticleTypes.WATER_SPLASH, pos);
      if (this.getThrower() instanceof EntityPlayer && mop.sideHit != null && this.getEntityWorld().isRemote == false) {
        world.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
      }
    }
    if (this.dimension != nether) {
      UtilSound.playSound(this.getEntityWorld(), pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS);
      // so far its both client and server
      if (this.getEntityWorld().isRemote == false) {
        if (pos != null) {
          if (UtilWorld.isAirOrWater(this.getEntityWorld(), pos)) {
            this.getEntityWorld().setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 3);
          }
          if (mop.sideHit != null) {
            BlockPos offset = pos.offset(mop.sideHit);
            if (offset != null && UtilWorld.isAirOrWater(this.getEntityWorld(), offset)) {
              this.getEntityWorld().setBlockState(offset, Blocks.FLOWING_WATER.getDefaultState(), 3);
            }
          }
        }
      }
    }
    this.setDead();
  }
}