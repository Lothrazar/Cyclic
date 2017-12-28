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
    Block block = te.getBlockType();
    GlStateManager.pushMatrix();
    float f = 0.6666667F;
    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
    //          GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
    //          GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
    //          this.model.signStick.showModel = false;
    //
    //      if (destroyStage >= 0)
    //      {
    //          this.bindTexture(DESTROY_STAGES[destroyStage]);
    //          GlStateManager.matrixMode(5890);
    //          GlStateManager.pushMatrix();
    //          GlStateManager.scale(4.0F, 2.0F, 1.0F);
    //          GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
    //          GlStateManager.matrixMode(5888);
    //      }
    //      else
    //      {
    //          this.bindTexture(SIGN_TEXTURE);
    //      }
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
    //      this.model.renderSign();
    GlStateManager.popMatrix();
    FontRenderer fontrenderer = this.getFontRenderer();
    float f3 = 0.010416667F;
    GlStateManager.translate(-2.0F, 1.33333334F, 0.046666667F);
    //180 so its not upside down
//    GlStateManager.rotate(180, 1, 0, 0);
    //below sets position
    GlStateManager.translate(1.5F, -1.3125F, -1.4375F);
          GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
    GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);//no idea what this does
    GlStateManager.depthMask(false);
    int i = 0;
    //    if (destroyStage < 0) {
    //      for (int j = 0; j < te.storage.length; ++j) {
    String s = "HI TWITTER!";
    //                  fontRenderer.drawString(text, 0, (int) (y * fontRenderer.FONT_HEIGHT), 0xFFFF00FF);
    fontrenderer.drawString(s, 0, 0, 0xFF0000);
    //      }
    //    }
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
