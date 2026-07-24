package com.lothrazar.cyclic.block.disenchant;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RenderDisenchant implements BlockEntityRenderer<TileDisenchant, RenderDisenchant.State> {

  public static class State extends BlockEntityRenderState {
    TileDisenchant blockEntity;
    float partialTick;
  }

  private static final Material BOOK_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS,
      Identifier.withDefaultNamespace("entity/enchanting_table_book"));
  private final BookModel bookModel;

  public RenderDisenchant(BlockEntityRendererProvider.Context ctx) {
    this.bookModel = new BookModel(ctx.bakeLayer(ModelLayers.BOOK));
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileDisenchant blockEntity, State state, float partialTicks, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
    state.partialTick = partialTicks;
  }

  @Override
  public void submit(State state, PoseStack pose, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileDisenchant tile = state.blockEntity;
    float partialTick = state.partialTick;
    if (tile.inputSlots.getStackInSlot(1).isEmpty()) {
      return;
    }
    pose.pushPose();
    //block is 12/16 tall, sit the book just above the top surface
    pose.translate(0.5F, 0.95F, 0.5F);
    float time = (tile.getLevel() == null ? 0F : tile.getLevel().getGameTime()) + partialTick;
    //gentle bobbing, same as vanilla
    pose.translate(0.0F, 0.1F + Mth.sin(time * 0.1F) * 0.01F, 0.0F);
    //upside-down: flip the book over on the X axis
    pose.mulPose(Axis.XP.rotationDegrees(180.0F));
    //slow continuous spin so the book is visibly alive
    float spin = time * 0.04F;
    pose.mulPose(Axis.YP.rotation(-spin));
    pose.mulPose(Axis.ZP.rotationDegrees(80.0F));
    //book is mostly open with a slight flutter
    float flip = time * 0.02F;
    float pageL = Mth.frac(flip + 0.25F) * 1.6F - 0.3F;
    float pageR = Mth.frac(flip + 0.75F) * 1.6F - 0.3F;
    pageL = Mth.clamp(pageL, 0.0F, 1.0F);
    pageR = Mth.clamp(pageR, 0.0F, 1.0F);
    this.bookModel.setupAnim(time, pageL, pageR, 0.2F);
    MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
    VertexConsumer vc = BOOK_LOCATION.buffer(buffer, RenderType::entitySolid);
    this.bookModel.renderToBuffer(pose, vc, state.lightCoords, 0);
    buffer.endBatch();
    pose.popPose();
  }
}
