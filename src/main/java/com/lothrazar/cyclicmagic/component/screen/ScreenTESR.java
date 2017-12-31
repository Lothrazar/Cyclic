package com.lothrazar.cyclicmagic.component.screen;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.text.ITextComponent;

public class ScreenTESR<T extends TileEntityScreen> extends BaseTESR<T> {
  private static final int MAX_WIDTH = 16;
  private static final int MAX_LINES = 8;
  public static final int MAX_TOTAL = MAX_WIDTH * MAX_LINES;
  final float horizDistFromCenter = 0.46F;
  final float leftColumn = 1.53F, rightColumn = 2.08F, rightEdge = 2.53F;
  final float width = rightEdge - leftColumn;
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
    String[] lines = new String[] { "brokeit" };
    try {
      lines = UtilChat.splitIntoLine(te.getText(), MAX_WIDTH);
    }
    catch (Exception e) {
      System.out.println("TODO use fontrenderer version ok");
    }
    boolean center = true;//TODO: ENUM justification left right center
    float lnWidth,  lnWidthFull = 96F;
    int ln = 0;
    for (String line : lines) {
      lnWidth =((float) this.getFontRenderer().getStringWidth(line))/lnWidthFull;
      if (center) {
//        System.out.println("lnWidth:"+line+lnWidth);
        float spRemainder = width - lnWidth;
        xt = leftColumn + spRemainder / 2;
      }
      renderTextAt(line, x, y, z, destroyStage, xt, yt, zt, angle, te.getColor());
      ln++;
      if (ln >= MAX_LINES) {
        break;
      }
      y += vOffset;
    }
  }
}
