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
  public static class FactoryDynMining implements IRenderFactory<EntityDynamiteMining> {
    @Override
    public Render<? super EntityDynamiteMining> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamiteMining>(rm, EntityDynamiteMining.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
  public static class FactoryDynSafe implements IRenderFactory<EntityDynamiteBlockSafe> {
    @Override
    public Render<? super EntityDynamiteBlockSafe> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamiteBlockSafe>(rm, EntityDynamiteBlockSafe.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
}
