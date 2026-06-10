package com.lothrazar.cyclic.block.shapebuilder;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.items.IItemHandler;

public class RenderStructure implements BlockEntityRenderer<TileStructure> {

  private static final float PREVIEW_ALPHA = 0.7f;

  private final BlockRenderDispatcher brd;

  public RenderStructure(BlockEntityRendererProvider.Context d) {
    this.brd = d.getBlockRenderDispatcher();
  }

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
        renderPreviewInWorld(te, matrixStack, ibuffer, partialTicks, destroyStage, stack);
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
  }

  private void renderPreviewInWorld(TileStructure te, PoseStack matrixStack, MultiBufferSource ibuffer, int packedLight, int packedOverlay, ItemStack stack) {
    if (stack.isEmpty()) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.9F, ClientConfigCyclic.getColor(te));
    }
    else {
      renderBlockPreview(te.getLevel(), te.getBlockPos(), te.getShape(), matrixStack, ibuffer, packedLight, packedOverlay, stack);
    }
  }

  // Renders ghost block previews at each shape position using the ibuffer passed by the
  // block entity render system (same approach as RenderCableFacade / FacadeUtil).
  // Uses Sheets.translucentCullBlockSheet() so alpha blending is enabled, and fetches
  // quads with RenderType.solid() so solid blocks (like planks) actually return quads.
  // renderModelBrightnessColorQuads lets us inject a custom alpha for the ghost effect.
  private void renderBlockPreview(Level level, BlockPos origin, List<BlockPos> positions, PoseStack poseStack, MultiBufferSource ibuffer, int packedLight, int packedOverlay, ItemStack stack) {
    BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
    if (state.getRenderShape() != RenderShape.MODEL) {
      return;
    }
    VertexConsumer consumer = ibuffer.getBuffer(Sheets.translucentCullBlockSheet());
    Minecraft mc = Minecraft.getInstance();
    for (BlockPos crd : positions) {
      poseStack.pushPose();
      poseStack.translate(crd.getX() - origin.getX(), crd.getY() - origin.getY(), crd.getZ() - origin.getZ());
      BakedModel model = brd.getBlockModel(state);
      int tint = mc.getBlockColors().getColor(state, level, crd, 0);
      float r = ((tint >> 16) & 255) / 255f;
      float g = ((tint >> 8) & 255) / 255f;
      float b = (tint & 255) / 255f;
      for (Direction dir : Direction.values()) {
        ModelData modelData = model.getModelData(level, crd, state, ModelData.EMPTY);
        List<BakedQuad> quads = model.getQuads(state, dir, level.random, modelData, RenderType.solid());
        RenderBlockUtils.renderModelBrightnessColorQuads(poseStack.last(), consumer, r, g, b, PREVIEW_ALPHA, quads, RenderBlockUtils.FULL_LIGHT, packedOverlay);
      }
      poseStack.popPose();
    }
  }
}
