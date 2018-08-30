package com.lothrazar.cyclicmagic.gui.core;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiButtonItemstack extends GuiButtonExt {

  private ItemStack stackRender = ItemStack.EMPTY;

  public GuiButtonItemstack(int buttonId, int x, int y) {
    super(buttonId, x, y, 20, 20, "");
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float p) {
    if (this.visible) {
      //render 
      super.drawButton(mc, mouseX, mouseY, p);
      ResourceLocation BUTTON_TEXTURES = new ResourceLocation(Const.MODID, "textures/gui/slot_magic.png");
      int k = this.getHoverState(this.hovered);
      GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x, this.y, 0, 0, this.width, this.height, 200, 20, 0, 0, 0, 0, this.zLevel);
      if (!stackRender.isEmpty())
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
