package com.lothrazar.cyclicmagic.core.gui;

import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GuiButtonItemstack extends GuiButtonTooltip {

  private ItemStack stackRender = ItemStack.EMPTY;

  public GuiButtonItemstack(int buttonId, int x, int y, int widthIn, int heightIn) {
    super(buttonId, x, y, widthIn, heightIn, "");
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float p) {
    if (this.visible) {
      super.drawButton(mc, mouseX, mouseY, p);
      //render 
      ModCyclic.proxy.renderItemOnScreen(this.stackRender, this.x + 2, this.y + 2);
    }
  }

  public ItemStack getStackRender() {
    return stackRender;
    
  }

  public void setStackRender(ItemStack stackRender) {
    this.stackRender = stackRender;
  }
}
