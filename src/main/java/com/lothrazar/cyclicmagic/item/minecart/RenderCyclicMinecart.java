/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.minecart;

import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCyclicMinecart<T extends EntityMinecart> extends RenderMinecart<T> {

  public static final FactoryGold FACTORY_GOLD = new FactoryGold();

  public static class FactoryGold implements IRenderFactory<EntityGoldMinecart> {

    @Override
    public Render<? super EntityGoldMinecart> createRenderFor(RenderManager rm) {
      return new RenderCyclicMinecart<EntityGoldMinecart>(rm, new ResourceLocation(Const.MODID, "textures/entity/gold_minecart.png"));
    }
  }

  public static final FactoryGoldFurnace FACTORY_GOLD_FURNACE = new FactoryGoldFurnace();

  public static class FactoryGoldFurnace implements IRenderFactory<EntityGoldFurnaceMinecart> {

    @Override
    public Render<? super EntityGoldFurnaceMinecart> createRenderFor(RenderManager rm) {
      return new RenderCyclicMinecart<EntityGoldFurnaceMinecart>(rm, new ResourceLocation(Const.MODID, "textures/entity/gold_minecart.png"));
    }
  }

  public static final FactoryStoneFurnace FACTORY_STONE_FURNACE = new FactoryStoneFurnace();

  public static class FactoryStoneFurnace implements IRenderFactory<EntityStoneMinecart> {

    @Override
    public Render<? super EntityStoneMinecart> createRenderFor(RenderManager rm) {
      return new RenderCyclicMinecart<EntityStoneMinecart>(rm, new ResourceLocation(Const.MODID, "textures/entity/stone_minecart.png"));
    }
  }

  public static final FactoryTurret FACTORY_TURRET = new FactoryTurret();

  public static class FactoryTurret implements IRenderFactory<EntityMinecartTurret> {

    @Override
    public Render<? super EntityMinecartTurret> createRenderFor(RenderManager rm) {
      return new RenderCyclicMinecart<EntityMinecartTurret>(rm, new ResourceLocation(Const.MODID, "textures/entity/gold_minecart.png"));
    }
  }

  //we might not have NEEDED to clone entire class, really its just texture swap eh
  //BUT inside the stupid factory we odnt know wha to pass it so now im forced to do thsi
  public RenderCyclicMinecart(RenderManager renderManagerIn, ResourceLocation t) {
    super(renderManagerIn);
    this.shadowSize = 0.5F;
    texture = t;
  }

  private ResourceLocation texture;// = new ResourceLocation(Const.MODID, "textures/entity/gold_minecart.png");
  protected ModelBase modelMinecart = new ModelMinecart();

  /**
   * Renders the desired {@code T} type Entity.
   */
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    this.bindEntityTexture(entity);
    long i = (long) entity.getEntityId() * 493286711L;
    i = i * i * 4392167121L + i * 98761L;
    float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
    GlStateManager.translate(f, f1, f2);
    double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
    double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
    double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
    double d3 = 0.30000001192092896D;
    Vec3d vec3d = entity.getPos(d0, d1, d2);
    float f3 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
    if (vec3d != null) {
      Vec3d vec3d1 = entity.getPosOffset(d0, d1, d2, d3);
      Vec3d vec3d2 = entity.getPosOffset(d0, d1, d2, -0.30000001192092896D);
      if (vec3d1 == null) {
        vec3d1 = vec3d;
      }
      if (vec3d2 == null) {
        vec3d2 = vec3d;
      }
      x += vec3d.x - d0;
      y += (vec3d1.y + vec3d2.y) / 2.0D - d1;
      z += vec3d.z - d2;
      Vec3d vec3d3 = vec3d2.addVector(-vec3d1.x, -vec3d1.y, -vec3d1.z);
      if (vec3d3.lengthVector() != 0.0D) {
        vec3d3 = vec3d3.normalize();
        entityYaw = (float) (Math.atan2(vec3d3.z, vec3d3.x) * 180.0D / Math.PI);
        f3 = (float) (Math.atan(vec3d3.y) * 73.0D);
      }
    }
    GlStateManager.translate((float) x, (float) y + 0.375F, (float) z);
    GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-f3, 0.0F, 0.0F, 1.0F);
    float f5 = (float) entity.getRollingAmplitude() - partialTicks;
    float f6 = entity.getDamage() - partialTicks;
    if (f6 < 0.0F) {
      f6 = 0.0F;
    }
    if (f5 > 0.0F) {
      GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0F * (float) entity.getRollingDirection(), 1.0F, 0.0F, 0.0F);
    }
    int j = entity.getDisplayTileOffset();
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor(entity));
    }
    IBlockState iblockstate = entity.getDisplayTile();
    if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
      GlStateManager.pushMatrix();
      this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      float f4 = 0.75F;
      GlStateManager.scale(f4, f4, f4);
      GlStateManager.translate(-0.5F, (float) (j - 8) / 16.0F, 0.5F);
      this.renderCartContents(entity, partialTicks, iblockstate);
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.bindEntityTexture(entity);
    }
    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
    this.modelMinecart.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
    GlStateManager.popMatrix();
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }

  /**
   * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
   */
  protected ResourceLocation getEntityTexture(T entity) {
    return texture;
  }

  protected void renderCartContents(T klass, float partialTicks, IBlockState blockState) {
    GlStateManager.pushMatrix();
    Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(blockState, klass.getBrightness());
    GlStateManager.popMatrix();
  }
}
