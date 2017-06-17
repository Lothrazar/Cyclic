package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * TODO: this can be reused for more TE's in future!!!
 * 
 * @author Sam
 *
 */
public class ButtonIncrementField extends GuiButtonTooltip {
  private BlockPos pos;
  private int field;
  public ButtonIncrementField(int buttonId, int x, int y, BlockPos p, int fld) {
    super(buttonId, x, y, 40, 20, "");
    this.pos = p;
    field = fld;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileIncrementField(pos, field));
    }
    return pressed;
  }
}
