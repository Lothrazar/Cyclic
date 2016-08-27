package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GuiBaseContainer extends GuiContainer {

  public GuiBaseContainer(Container inventorySlotsIn) {
    super(inventorySlotsIn);
  }

  public abstract String getTitle();
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String s = UtilChat.lang(getTitle());// "tile.placer_block.name"
    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
  }


}
