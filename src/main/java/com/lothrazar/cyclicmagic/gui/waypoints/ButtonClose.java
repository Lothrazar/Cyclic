package com.lothrazar.cyclicmagic.gui.waypoints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonClose extends GuiButton {
  public ButtonClose(int id, int x, int y) {
    super(id, x, y, 30, 20, I18n.format("button.close"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      mc.thePlayer.closeScreen();
    }
    return pressed;
  }
}
