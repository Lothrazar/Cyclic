package com.lothrazar.cyclicmagic.component.wandlightning;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityLightningballBolt extends EntityThrowableDispensable {
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
 
  public static class FactoryLightning implements IRenderFactory<EntityLightningballBolt> {
    @Override
    public Render<? super EntityLightningballBolt> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityLightningballBolt>(rm, "lightning");
    }
  }
}