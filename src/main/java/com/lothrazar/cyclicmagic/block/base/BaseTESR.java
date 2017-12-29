package com.lothrazar.cyclicmagic.block.base;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
    if (stack == null || stack.isEmpty()) {
      return;
    }
    GlStateManager.pushMatrix();
    //start of rotate
    GlStateManager.translate(.5, 0, .5);
    long angle = (System.currentTimeMillis() / 10) % 360;
    GlStateManager.rotate(angle, 0, 1, 0);
    GlStateManager.translate(-.5, 0, -.5);
    //end of rotate
    GlStateManager.translate(x, itemHeight, y);//move to xy center and up to top level
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
