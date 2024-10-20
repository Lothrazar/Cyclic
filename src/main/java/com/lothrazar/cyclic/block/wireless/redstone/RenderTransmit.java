package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.UtilRender;
import com.lothrazar.cyclic.util.UtilWorld;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class RenderTransmit extends TileEntityRenderer<TileWirelessTransmit> {

  public RenderTransmit(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public boolean isGlobalRenderer(TileWirelessTransmit te) {
    return true;
  }

  @Override
  public void render(TileWirelessTransmit te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    //    if (te.requiresRedstone() && !te.isPowered()) {
    //      return;
    //    }
    int previewType = te.getField(TileWirelessTransmit.Fields.RENDER.ordinal());
    if (previewType <= 0) {
      return;
    }
    List<BlockPos> shape = new ArrayList<>();
    String dimensionId = UtilWorld.dimensionToString(te.getWorld());
    for (int slot = 0; slot < te.inventory.getSlots(); slot++) {
      BlockPosDim dimPosSaved = te.getTargetInSlot(slot);
      if (dimPosSaved != null
          && dimPosSaved.getDimension().equalsIgnoreCase(dimensionId)) {
        shape.add(dimPosSaved.getPos());
      }
      //        draw(slot, te, matrixStack, iRenderTypeBuffer);
    }
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      UtilRender.renderOutline(te.getPos(), shape, matrixStack, 0.4F, ClientConfigCyclic.getColor(te));
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : shape) {
        UtilRender.createBox(matrixStack, crd, Vector3d.copy(te.getPos()));
      }
    }
  }
}
