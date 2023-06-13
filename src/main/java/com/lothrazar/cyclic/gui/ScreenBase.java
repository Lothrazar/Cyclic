package com.lothrazar.cyclic.gui;

import java.util.List;
import com.lothrazar.cyclic.api.IHasTooltip;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class ScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

  public ScreenBase(T screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  protected void drawBackground(GuiGraphics ms, ResourceLocation gui) {
    //    this.minecraft.getTextureManager().bind(gui);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, gui);
    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    ms.blit(gui, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    //  ScreenUtils.drawTexturedModalRect(ms, relX, relY, relY, relY, relY, relX, relY);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    for (GuiEventListener btn : this.children()) {
      Minecraft mc = Minecraft.getInstance();
      int mouseX = (int) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth());
      int mouseY = (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight());
      if (btn instanceof GuiSliderInteger && btn.isMouseOver(mouseX, mouseY)) {
        return btn.keyPressed(keyCode, scanCode, modifiers);
      }
    }
    for (GuiEventListener widget : this.children()) {
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

  protected void drawSlot(GuiGraphics ms, int x, int y, ResourceLocation texture) {
    drawSlot(ms, x, y, texture, 18);
  }

  protected void drawSlot(GuiGraphics ms, int x, int y, ResourceLocation texture, int size) {
    //    this.minecraft.getTextureManager().bind(texture);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, texture);
    ms.blit(texture, leftPos + x, topPos + y, 0, 0, size, size, size, size);
  }

  protected void drawSlot(GuiGraphics ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT, 18);
  }

  protected void drawSlotLarge(GuiGraphics ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT_LARGE, 26);
  }

  /**
   * Translate the block name; and draw it in the top center
   *
   * @param name
   */
  protected void drawName(GuiGraphics ms, String name) {
    drawString(ms, name, (this.getXSize() - this.font.width(name)) / 2, 6.0F);
  }

  protected void drawString(GuiGraphics gg, String name, float x, float y) {
    gg.drawString(font, ChatUtil.lang(name), x, y, 4210752, false);
  }

  public void drawButtonTooltips(GuiGraphics gg, int mouseX, int mouseY) {
    for (GuiEventListener btn : this.children()) {
      if (btn instanceof IHasTooltip && btn.isMouseOver(mouseX, mouseY)
          && btn instanceof AbstractWidget ww) {
        //   ww.render(gg, mouseX, mouseY, 0);
        List<Component> localTooltip = ((IHasTooltip) btn).getTooltips();
        if (localTooltip != null) {
          gg.renderComponentTooltip(font, localTooltip, mouseX - leftPos, mouseY - topPos);
        }
      }
    }
    for (GuiEventListener widget : this.children()) {
      if (widget instanceof IHasTooltip && widget.isMouseOver(mouseX, mouseY)) {
        IHasTooltip txt = (IHasTooltip) widget;
        if (txt.getTooltips() != null) {
          gg.renderComponentTooltip(font, txt.getTooltips(), mouseX - leftPos, mouseY - topPos);
        }
      }
    }
  }
}
