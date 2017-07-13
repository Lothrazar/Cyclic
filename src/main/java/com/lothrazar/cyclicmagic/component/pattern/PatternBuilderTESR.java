package com.lothrazar.cyclicmagic.component.pattern;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTesr;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PatternBuilderTESR extends BaseMachineTesr<TileEntityPatternBuilder> {
  public PatternBuilderTESR() {
    super();
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {}
  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage, float p) {
    super.render(te, x, y, z, partialTicks, destroyStage, p);
    if (te instanceof TileEntityPatternBuilder == false) { return; }
    TileEntityPatternBuilder tile = ((TileEntityPatternBuilder) te);
    if (tile.isPreviewVisible()) {
      UtilWorld.RenderShadow.renderBlockList(tile.getSourceShape(), te.getPos(), x, y, z, 0.7F, 0F, 1F);
      UtilWorld.RenderShadow.renderBlockList(tile.getTargetShape(), te.getPos(), x, y, z, 1F, 1F, 1F);
    }
  }
}
