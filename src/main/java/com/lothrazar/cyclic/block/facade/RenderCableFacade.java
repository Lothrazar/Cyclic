package com.lothrazar.cyclic.block.facade;

import com.lothrazar.cyclic.block.cable.CableTileBase;
import com.lothrazar.cyclic.config.ConfigRegistry;
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

public class RenderCableFacade implements BlockEntityRenderer<CableTileBase> {

  private BlockRenderDispatcher brd;
  private ModelBlockRenderer renderer;

  public RenderCableFacade(BlockEntityRendererProvider.Context d) {
    this.brd = d.getBlockRenderDispatcher();
    this.renderer = brd.getModelRenderer();
  }

  @Override
  public boolean shouldRenderOffScreen(CableTileBase te) {
    return true;
  }

  //  
  // THANK YOU https://forums.minecraftforge.net/topic/117560-solved-blockentity-rendering-too-dark/
  //
  @Override
  public void render(CableTileBase te, float v, PoseStack matrixStack, MultiBufferSource ibuffer,
      int packedLight, int packedOverlay) {
    if (te.getFacade() != null
        && ConfigRegistry.CABLE_FACADES.get()) {
      //
      BlockState facadeState = te.getFacadeState(te.getLevel());
      BakedModel model = this.brd.getBlockModel(facadeState);
      VertexConsumer vertexConsumer = VertexMultiConsumer.create(ibuffer.getBuffer(RenderType.solid()));
      renderer.tesselateBlock(te.getLevel(), model, facadeState, te.getBlockPos(),
          matrixStack, vertexConsumer, false, te.getLevel().random, packedLight, packedOverlay,
          ModelData.EMPTY, RenderType.solid());
      //
      // OLD WAY: it works but its WAY TOO BRIGHT
      // 
      //      brd.renderSingleBlock(facadeState, matrixStack, ibuffer, 0, packedOverlay,
      //          ModelData.EMPTY, RenderType.solid());
    }
  }
}
