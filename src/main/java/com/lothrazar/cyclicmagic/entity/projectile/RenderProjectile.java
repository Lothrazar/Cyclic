package com.lothrazar.cyclicmagic.entity.projectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProjectile<T extends Entity> extends RenderSnowball<T> {
  public RenderProjectile(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
    super(renderManagerIn, itemIn, itemRendererIn);
  }
  public static final FactoryLightning FACTORY_LIGHTNING = new FactoryLightning();
  public static class FactoryLightning implements IRenderFactory<EntityLightningballBolt> {
    @Override
    public Render<? super EntityLightningballBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityLightningballBolt>(rm, EntityLightningballBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryHarvest FACTORY_HARVEST = new FactoryHarvest();
  public static class FactoryHarvest implements IRenderFactory<EntityHarvestBolt> {
    @Override
    public Render<? super EntityHarvestBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityHarvestBolt>(rm, EntityHarvestBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryWater FACTORY_WATER = new FactoryWater();
  public static class FactoryWater implements IRenderFactory<EntityWaterBolt> {
    @Override
    public Render<? super EntityWaterBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityWaterBolt>(rm, EntityWaterBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactorySnow FACTORY_SNOW = new FactorySnow();
  public static class FactorySnow implements IRenderFactory<EntitySnowballBolt> {
    @Override
    public Render<? super EntitySnowballBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntitySnowballBolt>(rm, EntitySnowballBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryTorch FACTORY_TORCH = new FactoryTorch();
  public static class FactoryTorch implements IRenderFactory<EntityTorchBolt> {
    @Override
    public Render<? super EntityTorchBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityTorchBolt>(rm, EntityTorchBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryFish FACTORY_FISH = new FactoryFish();
  public static class FactoryFish implements IRenderFactory<EntityFishingBolt> {
    @Override
    public Render<? super EntityFishingBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityFishingBolt>(rm, EntityFishingBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryShear FACTORY_SHEAR = new FactoryShear();
  public static class FactoryShear implements IRenderFactory<EntityShearingBolt> {
    @Override
    public Render<? super EntityShearingBolt> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityShearingBolt>(rm, EntityShearingBolt.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryDungeon FACTORY_DUNG = new FactoryDungeon();
  public static class FactoryDungeon implements IRenderFactory<EntityDungeonEye> {
    @Override
    public Render<? super EntityDungeonEye> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDungeonEye>(rm, EntityDungeonEye.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryDyn FACTORY_DYN = new FactoryDyn();
  public static class FactoryDyn implements IRenderFactory<EntityDynamite> {
    @Override
    public Render<? super EntityDynamite> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamite>(rm, EntityDynamite.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryFire FACTORY_FIRE = new FactoryFire();
  public static class FactoryFire implements IRenderFactory<EntityBlazeBolt> {
    @Override
    public Render<? super EntityBlazeBolt> createRenderFor(RenderManager rm) {
      return new RenderSpell<EntityBlazeBolt>(rm);
    }
  }
  public static final FactoryDynMining FACTORY_DYNMINING = new FactoryDynMining();
  public static class FactoryDynMining implements IRenderFactory<EntityDynamiteMining> {
    @Override
    public Render<? super EntityDynamiteMining> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamiteMining>(rm, EntityDynamiteMining.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryDynSafe FACTORY_DYNSAVE = new FactoryDynSafe();
  public static class FactoryDynSafe implements IRenderFactory<EntityDynamiteBlockSafe> {
    @Override
    public Render<? super EntityDynamiteBlockSafe> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamiteBlockSafe>(rm, EntityDynamiteBlockSafe.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryBall FACTORY_BALL = new FactoryBall();
  public static class FactoryBall implements IRenderFactory<EntityMagicNetFull> {
    @Override
    public Render<? super EntityMagicNetFull> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityMagicNetFull>(rm, EntityMagicNetFull.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static final FactoryBallEmpty FACTORY_BALLEMPTY = new FactoryBallEmpty();
  public static class FactoryBallEmpty implements IRenderFactory<EntityMagicNetEmpty> {
    @Override
    public Render<? super EntityMagicNetEmpty> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityMagicNetEmpty>(rm, EntityMagicNetEmpty.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
}
