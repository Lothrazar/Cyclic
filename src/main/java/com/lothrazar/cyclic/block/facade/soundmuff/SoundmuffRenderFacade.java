package com.lothrazar.cyclic.block.facade.soundmuff;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class SoundmuffRenderFacade implements BlockEntityRenderer<SoundmuffTileFacade> {

  private BlockRenderDispatcher brd;

  public SoundmuffRenderFacade(BlockEntityRendererProvider.Context d) {
    this.brd = d.getBlockRenderDispatcher();
  }

  @Override
  public boolean shouldRenderOffScreen(SoundmuffTileFacade te) {
    return true;
  }

  @Override
  public void render(SoundmuffTileFacade te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    if (te.getFacade() != null) {
      BlockState facadeState = te.getFacadeState(te.getLevel());
      brd.renderSingleBlock(facadeState, matrixStack, ibuffer, partialTicks, destroyStage,
          ModelData.EMPTY, RenderType.solid());
    }
  }
}
