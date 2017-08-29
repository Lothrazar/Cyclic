package com.lothrazar.cyclicmagic.gui.button;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.net.PacketTileIncrementField;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Re-used by many tile entities
 * used to increment fields using packets
 * 
 * @author Sam
 *
 */
public class ButtonIncrementField extends GuiButtonTooltip {
  private BlockPos pos;
  private int field;
  private int value;
  public ButtonIncrementField(int buttonId, int x, int y, BlockPos p, int fld) {
    this(buttonId, x, y, p, fld, 1);
  }
  public ButtonIncrementField(int buttonId, int x, int y, BlockPos p, int fld, int diff) {
    this(buttonId, x, y, p, fld, diff, 40, 20);
  }
  public ButtonIncrementField(int buttonId, int x, int y,  BlockPos p,
      int fld, int diff,
      int w, int h) {
    super(buttonId, x, y, w, h, "");
    this.pos = p;
    field = fld;
    this.value = diff;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileIncrementField(pos, field, value));
    }
    return pressed;
  }
}
