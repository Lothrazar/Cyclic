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
package com.lothrazar.cyclicmagic.block.base;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class BaseTESR<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
  protected IModel model;
  protected IBakedModel bakedModel;
  protected String resource = null;
  public BaseTESR(@Nullable Block block) {
    if (block != null)
      resource = "tesr/" + block.getUnlocalizedName().replace("tile.", "").replace(".name", "");
  }
  protected IBakedModel getBakedModel() {
    // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
    if (bakedModel == null && resource != null) {
      try {
        model = ModelLoaderRegistry.getModel(new ResourceLocation(Const.MODID, resource));
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
          new Function<ResourceLocation, TextureAtlasSprite>() {
            @Override
            public TextureAtlasSprite apply(ResourceLocation location) {
              return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
            }
          });
    }
    return bakedModel;
  }
  protected void renderItem(TileEntity te, ItemStack stack, float itemHeight) {
    this.renderItem(te, stack, 0.5F, itemHeight, 0.5F);
  }
  protected void renderItem(TileEntity te, ItemStack stack, float x, float itemHeight, float y) {
    this.renderItem(te, stack, x, itemHeight, y, 0, true, 0.4F);
  }
  protected void renderItem(TileEntity te, ItemStack stack, float x, float itemHeight, float y, int initialAngle, boolean isSpinning, float scaleFactor) {
    //    GuiHelper.drawTexturedRect(minecraft, texture, x, y, width, height, zLevel, texPosX, texPosY, texWidth, texHeight);
    if (stack == null || stack.isEmpty()) {
      return;
    }
    GlStateManager.pushMatrix();
    //start of rotate
    if (initialAngle > 0) {
      GlStateManager.translate(.5, 0, .5);
      GlStateManager.rotate(initialAngle, 0, 1, 0);
      GlStateManager.translate(-.5, 0, -.5);
    }
    if (isSpinning) {
      GlStateManager.translate(.5, 0, .5);
      long angle = (System.currentTimeMillis() / 10) % 360;
      GlStateManager.rotate(angle, 0, 1, 0);
      GlStateManager.translate(-.5, 0, -.5);
    }
    //end of rotate
    GlStateManager.translate(x, itemHeight, y);//move to xy center and up to top level
    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);//shrink down
    // Thank you for helping me understand lighting @storagedrawers  https://github.com/jaquadro/StorageDrawers/blob/40737fb2254d68020a30f80977c84fd50a9b0f26/src/com/jaquadro/minecraft/storagedrawers/client/renderer/TileEntityDrawersRenderer.java#L96
    fixLighting(te);
    Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
    GlStateManager.popMatrix();
  }
  public void fixLighting(TileEntity te) {
    int ambLight = getWorld().getCombinedLight(te.getPos().offset(EnumFacing.UP), 0);
    if (ambLight == 0) {
      ambLight = 15728656;//if there is a block above blocking light, dont make it dark
    }
    int lu = ambLight % 65536;
    int lv = ambLight / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lu / 1.0F, (float) lv / 1.0F);
    //end of 'fix lighting'
  }
  /**
   * TextColor similar to int textColor = 0xFF0055;
   * 
   * @param s
   * @param x
   * @param y
   * @param z
   * @param destroyStage
   * @param xt
   * @param yt
   * @param zt
   * @param angle
   * @param textColor
   */
  public void renderTextAt(String s, double x, double y, double z, int destroyStage, float xt, float yt, float zt, float angle, int textColor) {
    boolean lightsOn = true;//TODO: toggle?
    GlStateManager.pushMatrix();
    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
    if (angle != 0) {
      GlStateManager.rotate(angle, 0, 1, 0);//rotate on Y axis to change which face it shows up on
    }
    //removeing rescaleNormal also makes it brighter
    if (lightsOn == false) {
      GlStateManager.enableRescaleNormal();
    }
    FontRenderer fontrenderer = this.getFontRenderer();
    //first two 0.5's move it away from center of block onto the edge, and top left corner
    GlStateManager.translate(-0.5F, 0.5F, 0.50009F); //extra 0.000X on the right coord to fix zindex fighting
    //below sets position
    GlStateManager.translate(xt, yt, zt);
    //sake makes it the right size do not touch
    //  float f3 = 0.010416667F;
    GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
    //if we skip the 3f line, its brighter. leave it in: darker
    if (lightsOn == false) {
      GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);//no idea what this does
    }
    GlStateManager.depthMask(false);
    fontrenderer.drawString(s, 0, 0, textColor);
    GlStateManager.depthMask(true);
    // GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
    if (destroyStage >= 0) {
      GlStateManager.matrixMode(5890);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
    }
  }
  public int angleOfFace(EnumFacing side) {
    switch (side) {
      case SOUTH:
        return 0;
      case EAST:
        return 90;
      case NORTH:
        return 180;
      case WEST:
        return 270;
      default:
        return -1;
    }
  }
}
