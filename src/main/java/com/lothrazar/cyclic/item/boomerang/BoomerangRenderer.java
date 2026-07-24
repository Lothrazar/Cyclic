package com.lothrazar.cyclic.item.boomerang;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BoomerangRenderer<T extends BoomerangEntity> extends EntityRenderer<T> {

  private static final float SPIN_DEGREES_PER_TICK = 30.0F;
  private final ItemRenderer itemRenderer;

  public BoomerangRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.itemRenderer = context.getItemRenderer();
    this.shadowRadius = 0.0F;
    this.shadowStrength = 0.0F;
  }

  @Override
  public void render(T entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int packedLight) {
    ms.pushPose();
    ItemStack stack = entity.getItem();
    BakedModel model = itemRenderer.getModel(stack, entity.level(), null, entity.getId() + 1);
    float spin = (entity.tickCount + partialTicks) * SPIN_DEGREES_PER_TICK;
    // outer: spin around world Y (horizontal spin like a real boomerang)
    ms.mulPose(Axis.YP.rotationDegrees(spin));
    // inner: tip the sprite flat so the face points up instead of at the camera
    ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
    itemRenderer.render(stack, ItemDisplayContext.FIXED, false, ms, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);
    ms.popPose();
    super.render(entity, entityYaw, partialTicks, ms, buffer, packedLight);
  }

  @Override
  public Identifier getTextureLocation(T entity) {
    return InventoryMenu.BLOCK_ATLAS;
  }
}
