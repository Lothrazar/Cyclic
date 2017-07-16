package com.lothrazar.cyclicmagic.component.entitydetector;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTesr;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityDetectorTESR extends BaseMachineTesr<TileEntityDetector> {
  public EntityDetectorTESR() {
    super();
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {}
  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage, float p) {
    super.render(te, x, y, z, partialTicks, destroyStage, p);
    if (te instanceof TileEntityDetector == false) { return; }
    TileEntityDetector tile = ((TileEntityDetector) te);
    if (tile.isPreviewVisible()) {
      UtilWorld.RenderShadow.renderBlockList(tile.getShape(), te.getPos(), x, y, z, 0.7F, 0F, 1F);
    }
  }
}
