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
package com.lothrazar.cyclicmagic.block.core;

import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class MachineTESR extends BaseMachineTESR<TileEntityBaseMachineInvo> {

  float itemRenderHeight = 0.99F;

  public MachineTESR(Block block, int slot) {
    super(block, slot);
  }

  public MachineTESR(Block block) {
    this(block, -1);
  }

  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage, float p) {
    super.render(te, x, y, z, partialTicks, destroyStage, p);
  }

  @Nullable
  protected IBakedModel getBakedModel() {
    // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
    if (bakedModel == null && resource != null) {
      try {
        model = ModelLoaderRegistry.getModel(new ResourceLocation(Const.MODID, resource));
      }
      catch (Exception e) {
        //should always exist, resources baked into mod
        //possibly unloaded chunk error? I have no idea?
        ModCyclic.logger.error("Error trying to obtain baked model", e);
        return null;
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

  protected void renderAnimation(@Nonnull TileEntityBaseMachineInvo te) {
    if (Minecraft.getMinecraft() == null
        || Minecraft.getMinecraft().getBlockRendererDispatcher() == null
        || Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer() == null
        || getBakedModel() == null) {
      ModCyclic.logger.error("TESR render animation caught by null check");
      return;
    }
    GlStateManager.pushMatrix();
    EnumFacing facing = te.getCurrentFacing();
    if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
      GlStateManager.rotate(90, 0, 1, 0);
      GlStateManager.translate(-1, 0, 0);//fix position and such
    }
    ////do the sliding across animation
    double currTenthOfSec = System.currentTimeMillis() / 100;//move speed
    double ratio = (currTenthOfSec % 8) / 10.00;//this is dong modulo 0.8 since there are 8 locations to move over
    GlStateManager.translate(0, 0, -1 * ratio);
    RenderHelper.disableStandardItemLighting();
    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    if (Minecraft.isAmbientOcclusionEnabled()) {
      GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }
    else {
      GlStateManager.shadeModel(GL11.GL_FLAT);
    }
    World world = te.getWorld();
    // Translate back to local view coordinates so that we can do the acual rendering here
    try {
      GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
      Tessellator tessellator = Tessellator.getInstance();
      //if buffer had an "isDrawing" here, i would halt if that is true
      tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
      //crash on line below, NPE, not sure why. very rare I guess?
      Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
          world,
          getBakedModel(),
          world.getBlockState(te.getPos()),
          te.getPos(),
          Tessellator.getInstance().getBuffer(), false);
      tessellator.draw();
    }
    catch (IllegalStateException alreadyBuilding) {
      ModCyclic.logger.info("Already building!  BufferBuilder:isDrawing == true I suppose: " + alreadyBuilding.getMessage());
      //BufferBuilder has a private flag "isDrawing", and if its true it throws this exceptoin
      //problem: there is no GET method or way to detect "is this drawing" before I start.
      //instead I catch and ignore this exception/
    }
    catch (Exception e) {
      ModCyclic.logger.error("TESR render baked model exception", e);
    }
    RenderHelper.enableStandardItemLighting();
    GlStateManager.popMatrix();
  }

  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    if (te == null) {
      return;
    }
    renderAnimation(te);
    if (this.itemSlotAbove >= 0) {
      ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
      if (stack.isEmpty() == false) {
        renderItem(te, stack, itemRenderHeight);
      }
    }
  }
}
