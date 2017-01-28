package com.lothrazar.cyclicmagic.block.tileentity;
import org.lwjgl.opengl.GL11;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial
 * http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public abstract class BaseMachineTesr<T extends TileEntityBaseMachineInvo> extends TileEntitySpecialRenderer<T> {
  private IModel model;
  private IBakedModel bakedModel;
  private String resource = null;
  protected int itemSlotAbove = -1;
  public BaseMachineTesr(String res, int slot) {
    this.resource = res;
    this.itemSlotAbove = slot;
  }
  public BaseMachineTesr( int slot) {
    this(null,slot);
  }
  /**
   * override this in your main class to call other animation hooks
   * 
   * @param te
   */
  public abstract void render(TileEntityBaseMachineInvo te);
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
  @Override
  public void renderTileEntityAt(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage) {
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    if (te.isRunning()) {
      this.render(te);
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
  protected void renderItem(TileEntityBaseMachineInvo te, ItemStack stack, float itemHeight) {
    GlStateManager.pushMatrix();
    //start of rotate
    GlStateManager.translate(.5, 0, .5);
    long angle = (System.currentTimeMillis() / 10) % 360;
    GlStateManager.rotate(angle, 0, 1, 0);
    GlStateManager.translate(-.5, 0, -.5);
    //end of rotate
    GlStateManager.translate(.5, itemHeight, .5);//move to xy center and up to top level
    float scaleFactor = 0.4f;
    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);//shrink down
    // Thank you for helping me understand lighting @storagedrawers  https://github.com/jaquadro/StorageDrawers/blob/40737fb2254d68020a30f80977c84fd50a9b0f26/src/com/jaquadro/minecraft/storagedrawers/client/renderer/TileEntityDrawersRenderer.java#L96
    //start of 'fix lighting' 
    int ambLight = getWorld().getCombinedLight(te.getPos().offset(EnumFacing.UP), 0);
    int lu = ambLight % 65536;
    int lv = ambLight / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lu / 1.0F, (float) lv / 1.0F);
    //end of 'fix lighting'
    Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
    GlStateManager.popMatrix();
  }
}
