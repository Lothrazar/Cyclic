package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderStructure extends TileEntityRenderer<TileStructure> {

  public RenderStructure(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileStructure te, float v, MatrixStack matrixStack, IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileStructure.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
      if (inv == null) {
        return;
      }
      ItemStack stack = inv.getStackInSlot(0);
      if (stack.isEmpty()) {
        UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.7F, ClientConfigCyclic.getColor(te));
      }
      else {
        UtilRender.renderAsBlock(te.getPos(), te.getShape(), matrixStack, stack, 1, 1);
      }
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShape()) {
        UtilRender.createBox(matrixStack, crd, Vector3d.copy(te.getPos()));
      }
    }
  }
}
