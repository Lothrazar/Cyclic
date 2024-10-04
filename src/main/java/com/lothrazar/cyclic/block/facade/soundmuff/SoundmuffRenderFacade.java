package com.lothrazar.cyclic.block.facade.soundmuff;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class SoundmuffRenderFacade implements BlockEntityRenderer<SoundmuffTileFacade> {

  private BlockRenderDispatcher brd;
  private ModelBlockRenderer renderer;

  public SoundmuffRenderFacade(BlockEntityRendererProvider.Context d) {
    this.brd = d.getBlockRenderDispatcher();
    this.renderer = brd.getModelRenderer();
  }

  @Override
  public boolean shouldRenderOffScreen(SoundmuffTileFacade te) {
    return true;
  }

  // TODO: this code is copy/pasted/reused in multiple facades
  @Override
  public void render(SoundmuffTileFacade te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int packedLight, int packedOverlay) {
    if (te.getFacade() != null) {
      BlockState facadeState = te.getFacadeState(te.getLevel());
      BakedModel model = this.brd.getBlockModel(facadeState);
      VertexConsumer vertexConsumer = VertexMultiConsumer.create(ibuffer.getBuffer(RenderType.solid()));
      renderer.tesselateBlock(te.getLevel(), model, facadeState, te.getBlockPos(),
          matrixStack, vertexConsumer, false, te.getLevel().random, packedLight, packedOverlay,
          ModelData.EMPTY, RenderType.solid());
    }
  }
}
