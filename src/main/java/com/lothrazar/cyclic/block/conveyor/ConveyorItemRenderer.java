package com.lothrazar.cyclic.block.conveyor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class ConveyorItemRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<ConveyorItemEntity> {

  private final ItemRenderer renderer;

  public ConveyorItemRenderer(EntityRenderDispatcher renderManager, ItemRenderer renderer) {
    super(renderManager);
    this.renderer = renderer;
    this.shadowRadius = 0.0F;
    this.shadowStrength = 0.0F;
  }

  @Override
  public void render(ConveyorItemEntity entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int packedLightIn) {
    ms.pushPose();
    ItemStack stack = entity.getItem();
    BakedModel model = this.renderer.getModel(stack, entity.level, null);
    this.renderer.render(stack, ItemTransforms.TransformType.GROUND, false, ms, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, model);
    ms.popPose();
    super.render(entity, entityYaw, partialTicks, ms, buffer, packedLightIn);
  }

  @Override
  public ResourceLocation getTextureLocation(ConveyorItemEntity entity) {
    return InventoryMenu.BLOCK_ATLAS;
  }

  @Override
  public boolean shouldRender(ConveyorItemEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
    return true;
  }
}
