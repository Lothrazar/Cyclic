package com.lothrazar.cyclicmagic.component.screen;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;

public class ScreenTESR<T extends TileEntityScreen> extends BaseTESR<T> {
  public static final int SCREEN_WIDTH = 96;
  // TODO: GUI selects how much padding to use? side padding and top padding? 
  private static final int MAX_WIDTH = 16;
  private static final int MAX_LINES = 8;
  public static final int MAX_TOTAL = MAX_WIDTH * MAX_LINES;
  public static final float rowHeight = -0.11F;// TODO: font size?
  public ScreenTESR(Block block) {
    super(block);
  }
  @SuppressWarnings("incomplete-switch")
  @Override
  public void render(TileEntityScreen te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    int tePadding = te.getPadding();
    float leftPadding = ((float) tePadding / 2) / 100F;
    //    ModCyclic.logger.log("padding "+padding);
    final float leftEdge = 0.0F + leftPadding, rightEdge = 1.05F;
    final float width = rightEdge - leftEdge;
    //default translations
    float xt = leftEdge, yt = 0, zt = 0;
    int angle = this.angleOfFace(te.getCurrentFacing());
    fixLighting(te);
    //    String[] lines = UtilChat.splitIntoLine(te.getText(), MAX_WIDTH);
    List<String> lines = UtilChat.splitIntoEqualLengths(this.getFontRenderer(), te.getText(), SCREEN_WIDTH - tePadding);
    //ModCyclic.logger.log("RENDER TEXT" +te.getText()+" line count "+lines.size());
    //now render
    float lnWidth;
    int ln = 0;
    TileEntityScreen.Justification justif = te.getJustification();
    for (String line : lines) {
      //  ModCyclic.logger.log("line  has width "+this.getFontRenderer().getStringWidth(line));
      //line = line.trim();//trim whitespaces on right side hey.. wait if line ends in space, and its right just, put space on next line?
      lnWidth = ((float) this.getFontRenderer().getStringWidth(line)) / ((float) SCREEN_WIDTH);
      switch (justif) {
        // LEFT has no changes
        case CENTER:
          float spRemainder = width - lnWidth;
          xt = leftEdge + spRemainder / 2;
        break;
        case RIGHT:
          float spRemainders = width - lnWidth;
          xt = leftEdge + spRemainders - 0.05F;//padding. why left doesnt need i dont know
        break;
      }
      renderTextAt(line, x, y, z, destroyStage, xt, yt, zt, angle, te.getColor());
      ln++;
      if (ln >= MAX_LINES) {
        break;
      }
      y += rowHeight;
    }
  }
}
