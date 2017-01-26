package com.lothrazar.cyclicmagic.block.tileentity;
import org.lwjgl.opengl.GL11;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
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
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class UncrafterTESR extends TileEntitySpecialRenderer<TileMachineUncrafter> {
  private IModel model;
  private IBakedModel bakedModel;
  private IBakedModel getBakedModel() {
    // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
    // for rendering
    if (bakedModel == null) {
      try {
        model = ModelLoaderRegistry.getModel(new ResourceLocation(Const.MODID, "block/uncrafting_block_west"));
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
  public void renderTileEntityAt(TileMachineUncrafter te, double x, double y, double z, float partialTicks, int destroyStage) {
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    // Render the rotating handles
    renderHandles(te);
    // Render our item
    renderItem(te);
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }
  private void renderHandles(TileMachineUncrafter te) {
    GlStateManager.pushMatrix();
    //two translates: one to move the axis and one to center it on the block
    GlStateManager.translate(.5, 0, .5);
    long angle = (System.currentTimeMillis() / 10) % 360;
    GlStateManager.rotate(angle, 0, 1, 0);
    GlStateManager.translate(-.5, 0, -.5);
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
  private void renderItem(TileMachineUncrafter te) {
    ItemStack stack = te.getStackInSlot(0);
    // System.out.println("renderItem"+stack);
    if (stack != null) {
      RenderHelper.enableStandardItemLighting();
      //            GlStateManager.enableLighting();
      GlStateManager.pushMatrix();
      // Translate to the center of the block and .9 points higher
      GlStateManager.translate(.5, 1.1, .5);
      GlStateManager.scale(.4f, .4f, .4f);
      Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
      GlStateManager.popMatrix();
    }
  }
}
