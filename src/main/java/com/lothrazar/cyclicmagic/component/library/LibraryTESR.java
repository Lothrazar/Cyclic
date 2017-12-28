package com.lothrazar.cyclicmagic.component.library;
import java.util.List;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class LibraryTESR<T extends TileEntityLibrary> extends BaseTESR<T> {
  public LibraryTESR(Block block) {
    super(block);
  }
  @Override
  public void render(TileEntityLibrary te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    //TODO: refactor so i can say
    //writeText(String, EnumFacing, u, v)
 //   for (int j = 0; j < te.storage.length; ++j) {

      renderOnSouthFace("Hi Twitter" , x,  y,  z, destroyStage);
      
  
   // }
  }
  //TODO: take in the face or render on all faces?
  private void renderOnSouthFace(String s, double x, double y, double z, int destroyStage) {

    GlStateManager.pushMatrix();
    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
    
    
    
    //initial setup
    float scaleTo = 0.6666667F;
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.scale(scaleTo, -1 * scaleTo, -1 * scaleTo);
    GlStateManager.popMatrix();
    FontRenderer fontrenderer = this.getFontRenderer();
    float f3 = 0.010416667F;
    
    GlStateManager.translate(-2.0F, 1.33333334F, 0.046666667F);
    //180 so its not upside down
    //    GlStateManager.rotate(180, 1, 0, 0);
    //below sets position
    GlStateManager.translate(1.6F, -1.4125F, 0.500005F);
    //sake makes it the right size do not touch
    GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
    GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);//no idea what this does
    GlStateManager.depthMask(false);
 
    fontrenderer.drawString(s, 0, 0, 0xFF0000);
    
    GlStateManager.depthMask(true);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
 
    
    
    
    GlStateManager.popMatrix();
    if (destroyStage >= 0) {
      GlStateManager.matrixMode(5890);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
    }
  }
}
