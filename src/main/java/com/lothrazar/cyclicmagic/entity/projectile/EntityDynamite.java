package com.lothrazar.cyclicmagic.entity.projectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityDynamite extends EntityThrowableDispensable {
  public static final float EX_CREEPER = 1;
  public static final float EX_CHARGEDCREEPER = 2;
  public static final float EX_TNT = 4;
  public static final float EX_ENDCRYSTAL = 6;
  public static final FactoryDyn FACTORY_DYN = new FactoryDyn();
  public static class FactoryDyn implements IRenderFactory<EntityDynamite> {
    @Override
    public Render<? super EntityDynamite> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityDynamite>(rm, "tnt");
    }
  }
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
    this.explosionLevel = strength;
  }
  public EntityDynamite(World worldIn, int strength, double x, double y, double z) {
    super(worldIn, x, y, z);
    this.explosionLevel = strength;
  }
  @Override
  protected void processImpact(RayTraceResult mop) {
    if (this.inWater == false) {
      this.getEntityWorld().createExplosion(this, this.posX, this.posY + (double) (this.height / 2.0F), this.posZ, explosionLevel, true);
    }
    this.setDead();
  }
}