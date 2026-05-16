package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.library.util.FacadeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class RenderLightFacade implements BlockEntityRenderer<TileLightFacade> {

  private BlockRenderDispatcher brd;
  private ModelBlockRenderer renderer;

  public RenderLightFacade(BlockEntityRendererProvider.Context d) {
    this.brd = d.getBlockRenderDispatcher();
    this.renderer = brd.getModelRenderer();
  }

  @Override
  public boolean shouldRenderOffScreen(TileLightFacade te) {
    return true;
  }

  @Override
  public void render(TileLightFacade te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int packedLight, int packedOverlay) {
    BlockState facadeState = te.getFacadeState(te.getLevel());
    FacadeUtil.renderBlockState(te.getLevel(), te.getBlockPos(), brd, renderer, ibuffer, matrixStack, facadeState, packedLight, packedOverlay);
  }
}
