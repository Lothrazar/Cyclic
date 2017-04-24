package com.lothrazar.cyclicmagic.component.enderbook;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonClose extends GuiButton {
  public ButtonClose(int id, int x, int y) {
    super(id, x, y, 30, 20, UtilChat.lang("button.close"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      mc.player.closeScreen();
    }
    return pressed;
  }
}
