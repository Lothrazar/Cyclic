package com.lothrazar.cyclicmagic.component.bucketstorage;

import org.lwjgl.opengl.GL11;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;

public class FluidTESR extends TileEntitySpecialRenderer<TileEntityBucketStorage> {
  @Override
  public void render(TileEntityBucketStorage te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
  Minecraft mc = Minecraft.getMinecraft();


FluidStack fluid = te.getCurrentFluid();
if(fluid == null) {
  return;
}
  
ModCyclic.logger.info(ModelFluid.WATER.toString());//minecraft:blocks/water_flow
 
 

ModCyclic.logger.info(fluid.getFluid().getStill(fluid).toString());
  //TextureAtlasSprite flowing = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
  
  World world = te.getWorld();
  // Translate back to local view coordinates so that we can do the acual rendering here
  GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
  Tessellator tessellator = Tessellator.getInstance();
  tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
  IModel model;
  try {
    model =ModelFluid.WATER;// ModelLoaderRegistry.getModel( fluid.getFluid().getStill(fluid) );
  }
  catch (Exception e) {
    throw new RuntimeException(e);
     
  }
  IBakedModel bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
      new Function<ResourceLocation, TextureAtlasSprite>() {
        @Override
        public TextureAtlasSprite apply(ResourceLocation location) {
          return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        }
      });
  
  Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
      world,
      bakedModel,
      world.getBlockState(te.getPos()),
      te.getPos(),
      Tessellator.getInstance().getBuffer(), false);
  tessellator.draw();
  RenderHelper.enableStandardItemLighting();
  GlStateManager.popMatrix();

//    FluidStack fluid = barrel.getCurrentFluid();
//    if(fluid == null) {
//      return;
//    }
// 
//    BlockPos pos = barrel.getPos();
// 
//
//    float height = 0.9f;
// 
 
//
//    Tessellator tessellator = Tessellator.getInstance();
//    BufferBuilder renderer = tessellator.getBuffer();
//    renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//    mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//
//    GlStateManager.pushMatrix();
//
//    RenderHelper.disableStandardItemLighting();
//    GlStateManager.enableBlend();
//    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
 
//      GL11.glShadeModel(GL11.GL_SMOOTH);
 
//    GlStateManager.translate(x, y, z);
//    GlStateManager.disableCull();
//
//   // TextureAtlasSprite sprite = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
//    int color = fluid.getFluid().getColor(fluid);
//    int brightness = mc.world.getCombinedLight(pos, fluid.getFluid().getLuminosity());
//
//
//    float size = 1.0F ;
//    
//    TextureAtlasSprite flowing = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());
//
//   // renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
 
 
////    renderer.setOverrideBlockTexture(fluid.getFluid().getBlock() );
////   // renderer.renderAllFaces = true;
////  ////  renderer.renderStandardBlock(flowing ? Block.waterMoving : Block.waterStill, x, y, z);
 
//    tessellator.draw();
//    GlStateManager.enableCull();
//
//    GlStateManager.disableBlend();
//    GlStateManager.popMatrix();
//    RenderHelper.enableStandardItemLighting();
  }
  
  
}
