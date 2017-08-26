package com.lothrazar.cyclicmagic.component.sprinkler;
import org.lwjgl.opengl.GL11;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public class SprinklerTESR<T extends TileSprinkler> extends BaseTESR<T> {
  public SprinklerTESR(Block block) {
    super(block);
  }
  @Override
  public void render(TileSprinkler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
  
      this.renderAnimation(te);
   
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }
  protected void renderAnimation(TileEntityBaseMachineInvo te) {
    GlStateManager.pushMatrix();
    if (te.isRunning()) {
    //start of rotate
    GlStateManager.translate(0.5, 0, 0.5);
    long angle = (System.currentTimeMillis() / 10) % 360;
    GlStateManager.rotate(angle, 0, 1, 0);
    GlStateManager.translate(-.5, 0, -.5);
    //end of rotate
    }
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
    GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
    Tessellator tessellator = Tessellator.getInstance();
    tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
        world,
        getBakedModel(),
        world.getBlockState(te.getPos()),
        te.getPos(),
        Tessellator.getInstance().getBuffer(), false);
    tessellator.draw();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.popMatrix();
  }
}
