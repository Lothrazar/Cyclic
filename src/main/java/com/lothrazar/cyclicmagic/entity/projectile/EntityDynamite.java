package com.lothrazar.cyclicmagic.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDynamite extends EntityThrowable {
  public static Item renderSnowball;
  public static final float EX_CREEPER = 1;
  public static final float EX_CHARGEDCREEPER = 2;
  public static final float EX_TNT = 4;
  public static final float EX_ENDCRYSTAL = 6;
  private float explosionLevel;
  public EntityDynamite(World worldIn) {
    super(worldIn);
    this.explosionLevel = EX_CREEPER;
  }
  public EntityDynamite(World worldIn, int explos) {
    super(worldIn);
    this.explosionLevel = explos;
  }
  public EntityDynamite(World worldIn, EntityLivingBase ent, int explos) {
    super(worldIn, ent);
    this.explosionLevel = explos;
  }
  public EntityDynamite(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    this.worldObj.createExplosion(this, this.posX, this.posY + (double) (this.height / 2.0F), this.posZ, explosionLevel, true);
    this.setDead();
  }
}