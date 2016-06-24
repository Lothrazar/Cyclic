package com.lothrazar.cyclicmagic.gui.player;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketFakeWorkbench;
import com.lothrazar.cyclicmagic.net.PacketOpenNormalInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTabToggleCrafting extends GuiButton {
  private GuiScreen gui;
  public ButtonTabToggleCrafting(GuiScreen g, int x, int y) {
    super(256, x, y, 10, 10, "C");
    gui = g;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      if (this.gui instanceof GuiInventory) {
        ModMain.network.sendToServer(new PacketFakeWorkbench(new NBTTagCompound()));
      }
      else {//if (this.gui instanceof GuiPlayerExtended || this.gui instanceof GuiCrafting) {
        this.gui.mc.displayGuiScreen(new GuiInventory(gui.mc.thePlayer));
        ModMain.network.sendToServer(new PacketOpenNormalInventory(this.gui.mc.thePlayer));
      }
    }
    return pressed;
  }
}
