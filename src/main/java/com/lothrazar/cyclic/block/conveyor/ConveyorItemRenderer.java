package com.lothrazar.cyclic.block.conveyor;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ConveyorItemRenderer<T extends Entity & IRendersAsItem> extends EntityRenderer<ConveyorItemEntity> {

  private final ItemRenderer renderer;

  public ConveyorItemRenderer(EntityRendererManager renderManager, ItemRenderer renderer) {
    super(renderManager);
    this.renderer = renderer;
    this.shadowSize = 0.0F;
    this.shadowOpaque = 0.0F;
  }

  @Override
  public void render(ConveyorItemEntity entity, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn) {
    ms.push();
    ItemStack stack = entity.getItem();
    IBakedModel model = this.renderer.getItemModelWithOverrides(stack, entity.world, null);
    this.renderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND, false, ms, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, model);
    ms.pop();
    super.render(entity, entityYaw, partialTicks, ms, buffer, packedLightIn);
  }

  @Override
  public ResourceLocation getEntityTexture(ConveyorItemEntity entity) {
    return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
  }
}
