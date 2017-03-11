package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.world.ExplosionBlockSafe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class EntityDynamite extends EntityThrowableDispensable {
  public static final float EX_CREEPER = 1;
  public static final float EX_CHARGEDCREEPER = 2;
  public static final float EX_TNT = 4;
  public static final float EX_ENDCRYSTAL = 6;
  public static Item renderSnowball;
  private float explosionLevel;
  public EntityDynamite(World worldIn) {
    super(worldIn);
    this.explosionLevel = EX_CREEPER;
  }
  public EntityDynamite(World worldIn, int explos) {
    super(worldIn);
    this.explosionLevel = explos;
  }
  public EntityDynamite(World worldIn, EntityLivingBase ent, int strength) {
    super(worldIn, ent);
    System.out.println("EntityDynamite constructor ");
    this.explosionLevel = strength;
  }
  public EntityDynamite(World worldIn, int strength, double x, double y, double z) {
    super(worldIn, x, y, z);
    System.out.println("EntityDynamite constructor ");
    this.explosionLevel = strength;
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    System.out.println("createExplosion");
    this.getEntityWorld().createExplosion(this, this.posX, this.posY + (double) (this.height / 2.0F), this.posZ, explosionLevel, true);
    this.setDead();
  }
}