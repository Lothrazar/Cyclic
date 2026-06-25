package com.lothrazar.cyclic.block.wireless.energy;

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

public class RenderWirelessEnergy implements BlockEntityRenderer<TileWirelessEnergy> {

  public RenderWirelessEnergy(BlockEntityRendererProvider.Context d) {}

  @Override
  public boolean shouldRenderOffScreen(TileWirelessEnergy te) {
    return true;
  }

  @Override
  public void render(TileWirelessEnergy te, float v, PoseStack matrix, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileWirelessEnergy.Fields.RENDER.ordinal());
    if (previewType <= 0) {
      return;
    }
    List<BlockPos> shape = new ArrayList<>();
    String dimensionId = LevelWorldUtil.dimensionToString(te.getLevel());
    for (int slot = 0; slot < te.gpsSlots.getSlots(); slot++) {
      BlockPosDim target = te.getTargetInSlot(slot);
      if (target != null && target.getDimension().equalsIgnoreCase(dimensionId)) {
        shape.add(target.getPos());
      }
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
