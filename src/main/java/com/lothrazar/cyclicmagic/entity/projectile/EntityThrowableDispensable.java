package com.lothrazar.cyclicmagic.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class EntityThrowableDispensable extends EntityThrowable {
  public EntityThrowableDispensable(World worldIn) {
    super(worldIn);
  }
  public EntityThrowableDispensable(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityThrowableDispensable(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (this.isDead) { return; }
    if (mop.entityHit != null && mop.entityHit instanceof EntityPlayer && mop.entityHit.world.isRemote) {
      //("thrower invalid");
      return;
    }
    this.processImpact(mop);
  }
  protected abstract void processImpact(RayTraceResult mop);
}
