package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;

public class ScreenTESR<T extends TileEntityScreen> extends BaseTESR<T> {
  private static final int MAX_WIDTH = 18;
  private static final int MAX_LINES = 8;
  final float horizDistFromCenter = 0.46F;
  final float leftColumn = 1.53F, rightColumn = 2.08F;
  final float topRow = -0.9F, bottomRow = -1.4125F;
  final float vOffset = -0.11F;
  public ScreenTESR(Block block) {
    super(block);
  }
  @Override
  public void render(TileEntityScreen te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    float xt = leftColumn, yt = topRow, zt = horizDistFromCenter;
    int angle = this.angleOfFace(te.getCurrentFacing());
    fixLighting(te);
    String[] lines = UtilChat.splitIntoLine(te.getText() , MAX_WIDTH);
    int ln = 0;
    for (String line : lines) {
      renderTextAt(line, x, y, z, destroyStage, xt, yt, zt, angle, te.getColor());
      ln++;
      if (ln >= MAX_LINES) {
        break;
      }
      y += vOffset;
    }
  }
}
