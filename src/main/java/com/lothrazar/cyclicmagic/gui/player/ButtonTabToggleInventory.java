package com.lothrazar.cyclicmagic.gui.player;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.playerworkbench.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.net.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.net.PacketOpenNormalInventory;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ButtonTabToggleInventory extends GuiButton {
  private GuiScreen gui;
  public ButtonTabToggleInventory(GuiScreen g, int x, int y) {
    super(51, x, y, 15, 10, "");
    gui = g;
    if (ClientProxy.keyExtraInvo != null && ClientProxy.keyExtraInvo.getDisplayName() != null &&
        ClientProxy.keyExtraInvo.getDisplayName().equals("NONE") == false) {
      this.displayString = ClientProxy.keyExtraInvo.getDisplayName();
    }
    else {
      this.displayString = "I";//the legacy one. in case someone is just running with the key unbound
    }
  }
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      if (this.gui instanceof GuiInventory || this.gui instanceof GuiPlayerExtWorkbench) {
        ModCyclic.network.sendToServer(new PacketOpenExtendedInventory());
      }
      else {//if (this.gui instanceof GuiPlayerExtended || this.gui instanceof GuiCrafting) {
        this.gui.mc.displayGuiScreen(new GuiInventory(gui.mc.player));
        ModCyclic.network.sendToServer(new PacketOpenNormalInventory(this.gui.mc.player));
      }
    }
    return pressed;
  }
}
