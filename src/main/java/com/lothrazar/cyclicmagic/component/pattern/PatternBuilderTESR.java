package com.lothrazar.cyclicmagic.component.pattern;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTESR;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PatternBuilderTESR extends BaseMachineTESR<TileEntityPatternBuilder> {
  public PatternBuilderTESR() {
    super();
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {}
  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage, float p) {
    super.render(te, x, y, z, partialTicks, destroyStage, p);
    if (te instanceof TileEntityPatternBuilder == false) {
      return;
    }
    TileEntityPatternBuilder tile = ((TileEntityPatternBuilder) te);
    if (tile.isPreviewVisible()) {
      UtilWorld.RenderShadow.renderBlockPos(tile.getSourceCenter().up(tile.getHeight() / 2), te.getPos(), x, y, z, 0F, 0F, 0.5F);
      UtilWorld.RenderShadow.renderBlockPos(tile.getTargetCenter().up(tile.getHeight() / 2), te.getPos(), x, y, z, .5F, 0, 0);
      UtilWorld.RenderShadow.renderBlockList(tile.getSourceFrameOutline(), te.getPos(), x, y, z, 0.7F, 0F, 1F);
      UtilWorld.RenderShadow.renderBlockList(tile.getTargetFrameOutline(), te.getPos(), x, y, z, 1F, 1F, 1F);
      UtilWorld.RenderShadow.renderBlockList(tile.getTargetShape(), te.getPos(), x, y, z, .1F, .1F, .1F);
    }
  }
}
