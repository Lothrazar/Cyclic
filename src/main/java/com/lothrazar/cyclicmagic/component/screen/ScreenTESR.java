package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.ModCyclic;
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
    float xt = leftColumn, yt = topRow, zt = horizDistFromCenter, angle = 90;
    //    int size = 10;
    //    GL11.glScalef(size,size,size);
    //    float mSize = (float)Math.pow(size,-1);
    String text = "write a book and put it in your pocket and try to split based off length ok lets go bby write a book and put it in your pocket and try to split based off length ok lets go bby";
    fixLighting(te);
    String[] lines = UtilChat.splitIntoLine(text, MAX_WIDTH);
    int ln = 0;
    for (String line : lines) {
      renderTextAt(line, x, y, z, destroyStage, xt, yt, zt, angle, 0xFFAA00);
      ln++;
      if (ln >= MAX_LINES) {
        break;
      }
      y += vOffset;
    }
    //    GL11.glScalef(mSize,mSize,mSize);
    //    int angle = angleOfFace(face);
    //    renderTextAt(stack.shortName(), x, y, z, destroyStage, xt, yt, zt, angle);
    //    if (stack.isEmpty() == false) {
    //      renderTextAt(stack.levelName(), x, y, z, destroyStage, xt, yt + vOffset, zt, angle);
    //      renderTextAt(stack.countName(), x, y, z, destroyStage, xt, yt + 2 * vOffset, zt, angle);
    //    }
  }
}
