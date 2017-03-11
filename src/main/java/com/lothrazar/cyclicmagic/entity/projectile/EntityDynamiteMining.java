package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.world.ExplosionMining;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDynamiteMining extends EntityThrowableDispensable {

  public static Item renderSnowball;
  private float explosionLevel;
  public EntityDynamiteMining(World worldIn) {
    super(worldIn);
    this.explosionLevel = 6;
  }
  public EntityDynamiteMining(World worldIn, int explos) {
    super(worldIn);
    this.explosionLevel = explos;
  }
  public EntityDynamiteMining(World worldIn, EntityLivingBase ent, int strength) {
    super(worldIn, ent);
    System.out.println("EntityDynamiteMining constructor ");
    this.explosionLevel = strength;
  }
  public EntityDynamiteMining(World worldIn, int strength, double x, double y, double z) {
    super(worldIn, x, y, z);
    System.out.println("EntityDynamiteMining constructor ");
    this.explosionLevel = strength;
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    System.out.println("ExplosionMining");
    ExplosionMining explosion = new ExplosionMining(this.getEntityWorld(), this.getThrower(), posX, posY, posZ, explosionLevel, false, true);
    explosion.doExplosionA();
    explosion.doExplosionB(false);

    this.setDead();
  }
}