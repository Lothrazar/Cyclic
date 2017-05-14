package com.lothrazar.cyclicmagic.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityLightningballBolt extends EntityThrowableDispensable {
  public static Item renderSnowball;
  public EntityLightningballBolt(World worldIn) {
    super(worldIn);
  }
  public EntityLightningballBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityLightningballBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void processImpact(RayTraceResult mop) {
    World world = getEntityWorld();
    EntityLightningBolt ball = new EntityLightningBolt(world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), false);
    world.spawnEntity(ball);
    this.setDead();
  }
}