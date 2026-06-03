package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;

public class RenderStructure implements BlockEntityRenderer<TileStructure> {

  public RenderStructure(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileStructure te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = CapabilityUtil.item(te.getLevel(),te.getBlockPos());
    if (inv == null) {
      return;
    }
    int previewType = te.getField(TileStructure.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      ItemStack stack = inv.getStackInSlot(0);
      try {
        renderPreviewInWorld(te, matrixStack, stack);
      }
      catch (NullPointerException e) {
        //handle unexpected & unsupported model types, for example: https://github.com/Lothrazar/Cyclic/issues/2473
        // java.lang.NullPointerException: Cannot invoke "net.minecraftforge.client.model.data.ModelData.derive()" because "data" is null
        ModCyclic.LOGGER.error("Error rendering preview: broken or unsupported model", e);
      }
      catch (Exception ex) {
        ModCyclic.LOGGER.error("Error in structure block preview", ex);
      }
    }
    if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShape()) {
        RenderBlockUtils.createBox(matrixStack, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShape()) {
        RenderBlockUtils.createBox(matrixStack, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }

  private static void renderPreviewInWorld(TileStructure te, PoseStack matrixStack, ItemStack stack) {
    if (stack.isEmpty()) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.9F, ClientConfigCyclic.getColor(te));
    }
    else {
      RenderBlockUtils.renderAsBlock(te.getLevel(), te.getBlockPos(), te.getShape(), matrixStack, stack, 1, 1);
    }
  }
}
