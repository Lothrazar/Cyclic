package com.lothrazar.cyclic.block.facade;

import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.library.util.FacadeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class RenderCableFacade implements BlockEntityRenderer<TileCableBase> {

  private BlockRenderDispatcher brd;
  private ModelBlockRenderer renderer;

  public RenderCableFacade(BlockEntityRendererProvider.Context d) {
    this.brd = d.getBlockRenderDispatcher();
    this.renderer = brd.getModelRenderer();
  }

  @Override
  public boolean shouldRenderOffScreen(TileCableBase te) {
    return true;
  }

  @Override
  public void render(TileCableBase te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int packedLight, int packedOverlay) {
    if (ConfigRegistry.CABLE_FACADES.get()) {
      BlockState facadeState = te.getFacadeState(te.getLevel());
      FacadeUtil.renderBlockState(te.getLevel(), te.getBlockPos(), brd, renderer, ibuffer, matrixStack, facadeState, packedLight, packedOverlay);
    }
  }
}
