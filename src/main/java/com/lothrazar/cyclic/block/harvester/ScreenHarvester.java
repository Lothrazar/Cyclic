package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenHarvester extends ScreenBase<ContainerHarvester> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger size;
  private GuiSliderInteger heightslider;

  public ScreenHarvester(ContainerHarvester screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileHarvester.MAX_ENERGY);
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TileHarvester.POWERCONF.get() > 0;
    int x = leftPos + 6;
    int y = topPos + 6;
    final int w = 120;
    final int h = 20;
    int f = TileHarvester.Fields.REDSTONE.ordinal();
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    y += 20;
    f = TileHarvester.Fields.RENDER.ordinal();
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //
    y += h;
    f = TileHarvester.Fields.DIRECTION.ordinal();
    btnDirection = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"));
    //now start sliders
    //
    x = leftPos + 30;
    y = topPos + 26;
    f = TileHarvester.Fields.HEIGHT.ordinal();
    heightslider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(), 0, TileHarvester.MAX_HEIGHT, menu.tile.getField(f)));
    heightslider.setTooltip(Tooltip.create(Component.translatable("buildertype.height.tooltip")));
    y += h + 4;
    f = TileHarvester.Fields.SIZE.ordinal();
    size = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(), 0, TileHarvester.MAX_SIZE, menu.tile.getField(f)));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    btnDirection.onValueUpdate(menu.tile);
    heightslider.setTooltip(Tooltip.create(Component.translatable("buildertype.height.tooltip")));
    size.setTooltip("cyclic.screen.size" + menu.tile.getField(size.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnDirection.visible = !menu.tile.getBlockStateVertical();
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
