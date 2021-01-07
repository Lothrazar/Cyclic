package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenItemCollector extends ScreenBase<ContainerItemCollector> {

  private ButtonMachineRedstone btnRedstone;
  private ButtonMachineRedstone btnRender;
  private GuiSliderInteger size;

  public ScreenItemCollector(ContainerItemCollector screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileItemCollector.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    btnRender = addButton(new ButtonMachineRedstone(x + 20, y, TileItemCollector.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    int w = 110;
    int h = 18;
    int f = TileItemCollector.Fields.SIZE.ordinal();
    x += 44;
    y += 8;
    size = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(), 0, 10, container.tile.getField(f)));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    size.setTooltip("cyclic.screen.size" + container.tile.getField(size.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int i = 0; i < 9; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, 41);
      this.drawSlot(ms, 7 + i * Const.SQ, 41 + Const.SQ);
    }
  }
}
