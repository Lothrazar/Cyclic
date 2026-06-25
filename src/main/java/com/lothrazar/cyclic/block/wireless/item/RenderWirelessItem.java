package com.lothrazar.cyclic.block.wireless.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RenderWirelessItem implements BlockEntityRenderer<TileWirelessItem> {

  public RenderWirelessItem(BlockEntityRendererProvider.Context d) {}

  @Override
  public boolean shouldRenderOffScreen(TileWirelessItem te) {
    return true;
  }

  @Override
  public void render(TileWirelessItem te, float v, PoseStack matrix, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileWirelessItem.Fields.RENDER.ordinal());
    if (previewType <= 0) {
      return;
    }
    List<BlockPos> shape = new ArrayList<>();
    String dimensionId = LevelWorldUtil.dimensionToString(te.getLevel());
    BlockPosDim target = te.getTargetInSlot();
    if (target != null && target.getDimension().equalsIgnoreCase(dimensionId)) {
      shape.add(target.getPos());
    }
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), shape, matrix, 0.9F, ClientConfigCyclic.getColor(te));
    } else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : shape) {
        RenderBlockUtils.createBox(matrix, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
