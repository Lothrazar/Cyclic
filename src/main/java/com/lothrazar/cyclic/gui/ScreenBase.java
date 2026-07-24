package com.lothrazar.cyclic.gui;

import com.lothrazar.library.core.IHasTooltip;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.ChatUtil;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public abstract class ScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

  public ScreenBase(T screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  public ScreenBase(T screenContainer, Inventory inv, Component titleIn, int imageWidth, int imageHeight) {
    super(screenContainer, inv, titleIn, imageWidth, imageHeight);
  }

  protected void drawBackground(GuiGraphicsExtractor ms, Identifier gui) {
    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    ms.blit(RenderPipelines.GUI_TEXTURED, gui, relX, relY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
  }

  @Override
  public boolean keyPressed(KeyEvent event) {
    for (GuiEventListener btn : this.children()) {
      Minecraft mc = Minecraft.getInstance();
      int mouseX = (int) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth());
      int mouseY = (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight());
      if (btn instanceof GuiSliderInteger && btn.isMouseOver(mouseX, mouseY)) {
        return btn.keyPressed(event);
      }
    }
    for (GuiEventListener widget : this.children()) {
      if (widget instanceof TextBoxAutosave) {
        //without this, txt boxes still work BUT:
        //keybindings like E OPEN INVENTORY dont make trigger the textbox, oops
        TextBoxAutosave txt = (TextBoxAutosave) widget;
        if (txt.isFocused()) {
          return txt.keyPressed(event);
        }
      }
    }
    return super.keyPressed(event);
  }

  protected void drawSlot(GuiGraphicsExtractor ms, int x, int y, Identifier texture) {
    drawSlot(ms, x, y, texture, Const.SQ);
  }

  protected void drawSlot(GuiGraphicsExtractor ms, int x, int y, Identifier texture, int size) {
    ms.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + x, topPos + y, 0, 0, size, size, size, size);
  }

  protected void drawSlot(GuiGraphicsExtractor ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT, Const.SQ);
  }

  protected void drawSlotLarge(GuiGraphicsExtractor ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT_LARGE, 26);
  }

  /**
   * Translate the block name; and draw it in the top center
   *
   * @param name
   */
  protected void drawName(GuiGraphicsExtractor ms, String name) {
    drawString(ms, name, (this.getXSize() - this.font.width(name)) / 2, 6.0F);
  }

  protected void drawString(GuiGraphicsExtractor gg, String name, float x, float y) {
    gg.text(font, ChatUtil.lang(name), (int) x, (int) y, 4210752, false);
  }

  public void drawButtonTooltips(GuiGraphicsExtractor gg, int mouseX, int mouseY) {
    Optional<TooltipComponent> noImage = Optional.empty();
    for (GuiEventListener btn : this.children()) {
      if (btn instanceof IHasTooltip ww && btn.isMouseOver(mouseX, mouseY)) {
        if (ww.getTooltips() != null) {
          gg.setTooltipForNextFrame(font, ww.getTooltips(), noImage, mouseX - leftPos, mouseY - topPos);
        }
      }
    }
    for (GuiEventListener widget : this.children()) {
      if (widget instanceof IHasTooltip txt && widget.isMouseOver(mouseX, mouseY)) {
        if (txt.getTooltips() != null) {
          gg.setTooltipForNextFrame(font, txt.getTooltips(), noImage, mouseX - leftPos, mouseY - topPos);
        }
      }
    }
  }

  /**
   * Propogate mouse drag events down to slider widgets
   */
  @Override
  public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
    for (GuiEventListener btn : this.children()) {
      if (btn.isMouseOver(event.x(), event.y()) && btn instanceof AbstractSliderButton) {
        ((AbstractSliderButton) btn).mouseDragged(event, dragX, dragY);
      }
    }
    return super.mouseDragged(event, dragX, dragY);
  }
}
