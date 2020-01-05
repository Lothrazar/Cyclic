package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.gui.ButtonMachine;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class ScreenBase<T extends Container> extends ContainerScreen<T> {

  public ScreenBase(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  protected void drawBackground(ResourceLocation gui) {
    this.minecraft.getTextureManager().bindTexture(gui);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
  }

  protected void drawSlot(int x, int y) {
    this.minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.SLOT);
    int relX = guiLeft + x;
    int relY = guiTop + y;
    int size = 18;
    blit(relX, relY, 0, 0, size, size, size, size);
  }

  public void drawButtonTooltips(int mouseX, int mouseY) {
    for (Widget btn : this.buttons) {
      if (btn instanceof ButtonMachine && btn.isMouseOver(mouseX, mouseY)) {
        btn.renderToolTip(mouseX, mouseY);
        this.renderTooltip(((ButtonMachine) btn).getTooltip(), mouseX - guiLeft, mouseY - guiTop);
      }
    }
  }
}
