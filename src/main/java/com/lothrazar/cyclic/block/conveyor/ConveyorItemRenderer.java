package com.lothrazar.cyclic.block.conveyor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ConveyorItemRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<ConveyorItemEntity> {

  private final ItemRenderer renderer;

  public ConveyorItemRenderer(EntityRendererProvider.Context renderManager) {
    super(renderManager);
    this.renderer = renderManager.getItemRenderer();
    this.shadowRadius = 0.0F;
    this.shadowStrength = 0.0F;
  }

  @Override
  public void render(ConveyorItemEntity entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int packedLightIn) {
    ms.pushPose();
    ItemStack stack = entity.getItem();
    BakedModel model = this.renderer.getModel(stack, entity.level(), null, entity.getId() + 1);
    if (model.isGui3d()) {
      // 3D block models (full blocks, slabs, pressure plates, etc.).
      // Their FIXED transform is designed for item frames and compounds badly with a manual rotation.
      // GROUND with no rotation shows them naturally; our renderer never calls the vanilla bob/spin
      // logic so they remain still on the belt.
      this.renderer.render(stack, ItemDisplayContext.GROUND, false, ms, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, model);
    } else {
      // Flat 2D sprite items (ingots, swords, food, doors, etc.).
      // FIXED shows the sprite face-on; rotating -90 around X tips it face-up on the belt.
      ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
      this.renderer.render(stack, ItemDisplayContext.FIXED, false, ms, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, model);
    }
    ms.popPose();
    super.render(entity, entityYaw, partialTicks, ms, buffer, packedLightIn);
  }

  @Override
  public Identifier getTextureLocation(ConveyorItemEntity entity) {
    return InventoryMenu.BLOCK_ATLAS;
  }

  @Override
  public boolean shouldRender(ConveyorItemEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
    return true;
  }
}
