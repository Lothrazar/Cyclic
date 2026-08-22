package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.gui.EnergyBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenForester extends ScreenBase<ContainerForester> {

  private ButtonMachineField btnRender;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private GuiSliderInteger size;
  private GuiSliderInteger heightslider;

  public ScreenForester(ContainerForester screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileForester.MAX);
    int x = leftPos + 6;
    int y = topPos + 6;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TileForester.POWERCONF.get() > 0;
    final int w = 120;
    final int h = 20;
    int f = TileForester.Fields.REDSTONE.ordinal();
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    y += h;
    f = TileForester.Fields.RENDER.ordinal();
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x = leftPos + 30;
    y = topPos + 36;
    f = TileForester.Fields.HEIGHT.ordinal();
    heightslider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, TileForester.Fields.HEIGHT.ordinal(), menu.tile.getBlockPos(),
        0, TileForester.MAX_HEIGHT, menu.tile.getField(f)));
    //
    y += h + 4;
    f = TileForester.Fields.SIZE.ordinal();
    size = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(), 0, 10, menu.tile.getField(f)));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    heightslider.setTooltip(Tooltip.create(Component.translatable("buildertype.height.tooltip")));
    size.setTooltip("cyclic.screen.size" + menu.tile.getField(size.getField()));
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    int relX = this.getXSize() / 2 - 9;
    energy.draw(ms, menu.getEnergy());
    int y = 16;
    //
    if (menu.tile.hasSapling()) {
      this.drawSlot(ms, relX, y);
    }
    else {
      this.drawSlot(ms, relX, y, TextureRegistry.SLOT_SAPLING, Const.SQ);
    }
  }
}
