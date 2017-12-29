package com.lothrazar.cyclicmagic.block.base;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public abstract class BaseMachineTESR<T extends TileEntityBaseMachineInvo> extends BaseTESR<T> {
  protected int itemSlotAbove = -1;
  public BaseMachineTESR(Block res, int slot) {
    super(res);
    this.itemSlotAbove = slot;
  }
  public BaseMachineTESR(int slot) {
    this(null, slot);
  }
  public BaseMachineTESR() {
    this(null, -1);
  }
  /**
   * override this in your main class to call other animation hooks
   * 
   * @param te
   */
  public abstract void renderBasic(TileEntityBaseMachineInvo te);
  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z,
      float partialTicks, int destroyStage, float alpha
  //, net.minecraft.client.renderer.BufferBuilder buffer
  ) {
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    if (te.isRunning()) {// && te.hasEnoughFuel()
      this.renderBasic(te);
    }
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }
  protected void renderAnimation(TileEntityBaseMachineInvo te) {
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
