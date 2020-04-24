package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.client.gui.IGuiEventListener;
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
    this.minecraft.getTextureManager().bindTexture(TextureRegistry.SLOT);
    int relX = guiLeft + x;
    int relY = guiTop + y;
    int size = 18;
    blit(relX, relY, 0, 0, size, size, size, size);
  }

  protected void drawSlotLarge(int x, int y) {
    this.minecraft.getTextureManager().bindTexture(TextureRegistry.SLOT_LARGE);
    int relX = guiLeft + x;
    int relY = guiTop + y;
    int size = 26;
    blit(relX, relY, 0, 0, size, size, size, size);
  }

  /**
   * Translate the block name; and draw it in the top center
   * 
   * @param name
   */
  protected void drawName(String name) {
    name = UtilChat.lang("block." + ModCyclic.MODID + "." + name);
    this.font.drawString(name,
        (this.getXSize() - this.font.getStringWidth(name)) / 2,
        6.0F, 4210752);
  }

  public void drawButtonTooltips(int mouseX, int mouseY) {
    for (Widget btn : this.buttons) {
      if (btn instanceof ButtonMachine && btn.isMouseOver(mouseX, mouseY)) {
        btn.renderToolTip(mouseX, mouseY);
        this.renderTooltip(((ButtonMachine) btn).getTooltip(), mouseX - guiLeft, mouseY - guiTop);
      }
    }
    for (IGuiEventListener widget : this.children) {
      if (widget instanceof TextboxInteger && widget.isMouseOver(mouseX, mouseY)) {
        TextboxInteger txt = (TextboxInteger) widget;
        this.renderTooltip(txt.getTooltip(), mouseX - guiLeft, mouseY - guiTop);
      }
    }
  }
}
