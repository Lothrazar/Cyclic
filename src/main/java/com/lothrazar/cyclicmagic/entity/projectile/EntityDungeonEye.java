package com.lothrazar.cyclicmagic.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDungeonEye extends EntityThrowable {
  public static Item renderSnowball;
  private double targetX;
  private double targetY;
  private double targetZ;
  public EntityDungeonEye(World worldIn) {
    super(worldIn);
  }
  public EntityDungeonEye(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityDungeonEye(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public void moveTowards(BlockPos pos) {
    this.targetX = (double) pos.getX();
    this.targetY = pos.getY();
    this.targetZ = (double) pos.getZ();
    this.setThrowableHeading(this.targetX, this.targetY, this.targetZ, (float) (this.getGravityVelocity()), 0.01F);
  }
  @Override
  public void onUpdate() {
    super.onUpdate();
    if (!this.worldObj.isRemote) {
      float mHoriz = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      double distX = this.targetX - this.posX;
      double distY = this.targetY - this.posY;
      double distZ = this.targetZ - this.posZ;
      float distLine = (float) Math.sqrt(distX * distX + distZ * distZ);
      float f2 = (float) Math.atan2(distZ, distX);
      double d2 = (double) mHoriz + (double) (distLine - mHoriz) * 0.0025D;
      if (distLine < 1.0F) {
        d2 *= 0.8D;
        // this.motionY *= 0.8D;//disabling gravity
      }
      else if (distLine > 30) { //if its far far away, slightly increase HSPEED
        //test increasing hspeed
        d2 *= 1.3;
      }
      //so overall its kind of asymptotic-ish to the right angle going out from the player, then vertical to the spawner
      this.motionX = Math.cos((double) f2) * d2;
      this.motionZ = Math.sin((double) f2) * d2;
      // the vertical speed gets faster, the closer you are to it horizontally
      double vanillaFactor = 0.014999999664723873D;// using a const pulled from
      // vanilla endereye
      this.motionY = (14 * distY) / distLine * vanillaFactor;
      if (this.posY < this.targetY) {
        // make sure motion is going up
        if (this.motionY < 0) {
          this.motionY *= -1;
        }
      }
      else {
        // make sure motion is going DOWN
        if (this.motionY > 0) {
          this.motionY *= -1;
        }
      }
    }
    float f3 = 0.25F;
    for (int i = 0; i < particleCount; ++i) {
      this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * (double) f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double) f3 - 0.5D, this.posZ - this.motionZ * (double) f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
    }
  }
  private final static int particleCount = 22;
  @Override
  protected void onImpact(RayTraceResult mop) {
    this.setDead();// does not pass through walls or entities
  }
}
