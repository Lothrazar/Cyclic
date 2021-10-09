package com.lothrazar.cyclic.base;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.IHasTooltip;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
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

  protected void drawBackground(PoseStack ms, ResourceLocation gui) {
    //    this.minecraft.getTextureManager().bind(gui);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, gui);
    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    this.blit(ms, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
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

  protected void drawSlot(PoseStack ms, int x, int y, ResourceLocation texture) {
    drawSlot(ms, x, y, texture, 18);
  }

  protected void drawSlot(PoseStack ms, int x, int y, ResourceLocation texture, int size) {
    //    this.minecraft.getTextureManager().bind(texture);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, texture);
    blit(ms, leftPos + x, topPos + y, 0, 0, size, size, size, size);
  }

  protected void drawSlot(PoseStack ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT, 18);
  }

  protected void drawSlotLarge(PoseStack ms, int x, int y) {
    drawSlot(ms, x, y, TextureRegistry.SLOT_LARGE, 26);
  }

  /**
   * Translate the block name; and draw it in the top center
   *
   * @param name
   */
  protected void drawName(PoseStack ms, String name) {
    name = UtilChat.lang("block." + ModCyclic.MODID + "." + name);
    drawString(ms, name,
        (this.getXSize() - this.font.width(name)) / 2,
        6.0F);
  }

  protected void drawString(PoseStack ms, String name, float x, float y) {
    this.font.draw(ms, UtilChat.lang(name), x, y, 4210752);
  }

  public void drawButtonTooltips(PoseStack ms, int mouseX, int mouseY) {
    for (GuiEventListener btn : this.children()) {
      if (btn instanceof IHasTooltip && btn.isMouseOver(mouseX, mouseY)
          && btn instanceof AbstractWidget) {
        ((AbstractWidget) btn).renderToolTip(ms, mouseX, mouseY);
        List<Component> localTooltip = ((IHasTooltip) btn).getTooltip();
        if (localTooltip != null) {
          this.renderComponentTooltip(ms, localTooltip, mouseX - leftPos, mouseY - topPos);
        }
      }
    }
    for (GuiEventListener widget : this.children()) {
      if (widget instanceof IHasTooltip && widget.isMouseOver(mouseX, mouseY)) {
        IHasTooltip txt = (IHasTooltip) widget;
        if (txt.getTooltip() != null) {
          this.renderComponentTooltip(ms, txt.getTooltip(), mouseX - leftPos, mouseY - topPos);
        }
      }
    }
  }
}
