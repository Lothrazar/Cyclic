package com.lothrazar.cyclicmagic.gui.button;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiCheckboxTooltip;
import com.lothrazar.cyclicmagic.net.PacketTileFacingToggle;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonToggleFacing extends GuiCheckboxTooltip {
  private BlockPos pos;
  private EnumFacing field;
  public ButtonToggleFacing(int buttonId, int x, int y, BlockPos p, EnumFacing fld, int w, int h) {
    super(buttonId, x, y, "", true);
    this.pos = p;
    field = fld;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileFacingToggle(pos, field));
    }
    return pressed;
  }
}
