package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.block.tileentity.MachineTESR;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilWorld;

public class StructureBuilderTESR extends MachineTESR {
  public StructureBuilderTESR(String block, int slot) {
    super(block, slot);
  }
  @Override
  public void renderTileEntityAt(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage) {
    super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    if (te instanceof TileEntityStructureBuilder == false) { return; }
    TileEntityStructureBuilder tile = ((TileEntityStructureBuilder) te);
    if (tile.renderOn()) {
      UtilWorld.RenderShadow.renderBlockList(tile.rebuildShape(), te.getPos(), x, y, z, 0.7F, 0F, 1F);
    }
  }
}
