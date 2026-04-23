package com.lothrazar.cyclic.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class FacadeUtil {

  public static void renderBlockState(Level level, BlockPos pos, BlockRenderDispatcher brd, ModelBlockRenderer renderer,
      MultiBufferSource ibuffer, PoseStack matrixStack, BlockState facadeState, int packedLight, int packedOverlay) {
    if (facadeState == null) {
      return;
    }
    BakedModel model = brd.getBlockModel(facadeState);
    VertexConsumer vertexConsumer = VertexMultiConsumer.create(ibuffer.getBuffer(RenderType.solid()));
    renderer.tesselateBlock(level, model, facadeState, pos,
        matrixStack, vertexConsumer, false, level.random, packedLight, packedOverlay,
        ModelData.EMPTY, RenderType.solid());
  }
}
