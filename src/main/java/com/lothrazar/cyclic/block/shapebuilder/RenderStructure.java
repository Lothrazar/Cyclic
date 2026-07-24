package com.lothrazar.cyclic.block.shapebuilder;

import java.util.ArrayList;
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
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import org.jspecify.annotations.Nullable;

public class RenderStructure implements BlockEntityRenderer<TileStructure, RenderStructure.State> {

  private static final float PREVIEW_ALPHA = 0.7f;

  public static class State extends BlockEntityRenderState {
    TileStructure blockEntity;
  }

  public RenderStructure(BlockEntityRendererProvider.Context d) {}

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileStructure blockEntity, State state, float partialTicks, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileStructure te = state.blockEntity;
    IItemHandler inv = CapabilityUtil.item(te.getLevel(), te.getBlockPos());
    if (inv == null) {
      return;
    }
    int previewType = te.getField(TileStructure.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      ItemStack stack = inv.getStackInSlot(0);
      MultiBufferSource.BufferSource ibuffer = Minecraft.getInstance().renderBuffers().bufferSource();
      try {
        renderPreviewInWorld(te, matrixStack, ibuffer, state.lightCoords, 0, stack);
      }
      catch (NullPointerException e) {
        //handle unexpected & unsupported model types, for example: https://github.com/Lothrazar/Cyclic/issues/2473
        ModCyclic.LOGGER.error("Error rendering preview: broken or unsupported model", e);
      }
      catch (Exception ex) {
        ModCyclic.LOGGER.error("Error in structure block preview", ex);
      }
      ibuffer.endBatch();
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
  // quads via the modern BlockStateModelSet/collectParts pipeline (BlockRenderDispatcher and
  // BakedModel#getQuads are gone in 26.1). renderModelBrightnessColorQuads lets us inject a
  // custom alpha for the ghost effect; tint is left neutral (white) since this is a preview overlay.
  private void renderBlockPreview(Level level, BlockPos origin, List<BlockPos> positions, PoseStack poseStack, MultiBufferSource ibuffer, int packedLight, int packedOverlay, ItemStack stack) {
    BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
    if (state.getRenderShape() != RenderShape.MODEL) {
      return;
    }
    VertexConsumer consumer = ibuffer.getBuffer(Sheets.translucentCullBlockSheet());
    BlockAndTintGetter btg = level instanceof BlockAndTintGetter g ? g : BlockAndTintGetter.EMPTY;
    BlockStateModelSet modelSet = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
    BlockStateModel model = modelSet.get(state);
    for (BlockPos crd : positions) {
      poseStack.pushPose();
      poseStack.translate(crd.getX() - origin.getX(), crd.getY() - origin.getY(), crd.getZ() - origin.getZ());
      List<BlockStateModelPart> parts = new ArrayList<>();
      model.collectParts(btg, crd, state, level.getRandom(), parts);
      List<BakedQuad> quads = new ArrayList<>();
      for (BlockStateModelPart part : parts) {
        for (Direction dir : Direction.values()) {
          quads.addAll(part.getQuads(dir));
        }
        quads.addAll(part.getQuads(null));
      }
      RenderBlockUtils.renderModelBrightnessColorQuads(poseStack.last(), consumer, 1F, 1F, 1F, PREVIEW_ALPHA, quads, RenderBlockUtils.FULL_LIGHT, packedOverlay);
      poseStack.popPose();
    }
  }
}
