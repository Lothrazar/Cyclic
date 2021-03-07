package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.IHasTooltip;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import net.minecraft.client.Minecraft;
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

  protected void drawBackground(MatrixStack ms, ResourceLocation gui) {
    this.minecraft.getTextureManager().bindTexture(gui);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(ms, relX, relY, 0, 0, this.xSize, this.ySize);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    for (Widget btn : this.buttons) {
      Minecraft mc = Minecraft.getInstance();
      int mouseX = (int) (mc.mouseHelper.getMouseX() * mc.getMainWindow().getScaledWidth() / mc.getMainWindow().getWidth());
      int mouseY = (int) (mc.mouseHelper.getMouseY() * mc.getMainWindow().getScaledHeight() / mc.getMainWindow().getHeight());
      if (btn instanceof GuiSliderInteger && btn.isMouseOver(mouseX, mouseY)) {
        return btn.keyPressed(keyCode, scanCode, modifiers);
      }
    }
    for (IGuiEventListener widget : this.children) {
      if (widget instanceof TextBoxAutosave) {
        //without this, txt boxes still work BUT:
        //keybindings like E OPEN INVENTORY dont make trigger the textbox, oops
        TextBoxAutosave txt = (TextBoxAutosave) widget;
        if (txt.isFocused()) {
          return txt.keyPressed(keyCode, scanCode, modifiers);
        }
      }
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  protected void drawSlot(MatrixStack ms, int x, int y, ResourceLocation texture, int size) {
    this.minecraft.getTextureManager().bindTexture(texture);
    blit(ms, guiLeft + x, guiTop + y, 0, 0, size, size, size, size);
  }

  protected void drawSlot(MatrixStack ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT, 18);
  }

  protected void drawSlotLarge(MatrixStack ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT_LARGE, 26);
  }

  /**
   * Translate the block name; and draw it in the top center
   * 
   * @param name
   */
  protected void drawName(MatrixStack ms, String name) {
    name = UtilChat.lang("block." + ModCyclic.MODID + "." + name);
    drawString(ms, name,
        (this.getXSize() - this.font.getStringWidth(name)) / 2,
        6.0F);
  }

  protected void drawString(MatrixStack ms, String name, float x, float y) {
    this.font.drawString(ms, UtilChat.lang(name), x, y, 4210752);
  }

  public void drawButtonTooltips(MatrixStack ms, int mouseX, int mouseY) {
    for (Widget btn : this.buttons) {
      if (btn instanceof IHasTooltip && btn.isMouseOver(mouseX, mouseY)) {
        btn.renderToolTip(ms, mouseX, mouseY);
        List<ITextComponent> localTooltip = ((IHasTooltip) btn).getTooltip();
        if (localTooltip != null) {
          this.func_243308_b(ms, localTooltip, mouseX - guiLeft, mouseY - guiTop);
        }
      }
    }
    for (IGuiEventListener widget : this.children) {
      if (widget instanceof IHasTooltip && widget.isMouseOver(mouseX, mouseY)) {
        IHasTooltip txt = (IHasTooltip) widget;
        if (txt.getTooltip() != null) {
          this.func_243308_b(ms, txt.getTooltip(), mouseX - guiLeft, mouseY - guiTop);
        }
      }
    }
  }
}
