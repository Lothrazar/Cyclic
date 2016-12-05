package com.lothrazar.cyclicmagic.gui.user;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketTileUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonUserAction extends GuiButton {
  private BlockPos pos;
  public ButtonUserAction(int buttonId, int x, int y, BlockPos p) {
    super(buttonId, x, y, 40, 20, "");
    this.pos = p;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileUser(pos));
    }
    return pressed;
  }
}
