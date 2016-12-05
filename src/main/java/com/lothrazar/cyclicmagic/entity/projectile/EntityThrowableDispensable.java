package com.lothrazar.cyclicmagic.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.MathHelper;
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

//  //from EntityFireball
//  public void setHeading(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
//  {
//  
//      this.setSize(1.0F, 1.0F);
//      this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
//      this.setPosition(x, y, z);
//      double d0 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
//      this.accelerationX = accelX / d0 * 0.1D;
//      this.accelerationY = accelY / d0 * 0.1D;
//      this.accelerationZ = accelZ / d0 * 0.1D;
//  }
}
