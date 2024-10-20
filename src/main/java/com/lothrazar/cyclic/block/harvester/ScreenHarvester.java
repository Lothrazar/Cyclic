package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenHarvester extends ScreenBase<ContainerHarvester> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger size;
  private GuiSliderInteger heightslider;

  public ScreenHarvester(ContainerHarvester screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileHarvester.MAX_ENERGY);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    energy.visible = TileHarvester.POWERCONF.get() > 0;
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileHarvester.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    y += 20;
    btnRender = addButton(new ButtonMachineField(x, y, TileHarvester.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //
    int f = TileHarvester.Fields.DIRECTION.ordinal();
    y += 20;
    btnDirection = addButton(new ButtonMachineField(x, y, f,
        container.tile.getPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"));
    final int w = 110;
    final int h = 18;
    //now start sliders
    //
    //
    y = guiTop + 24;
    x = guiLeft + 34;
    f = TileHarvester.Fields.HEIGHT.ordinal();
    heightslider = this.addButton(new GuiSliderInteger(x, y, w, h, TileHarvester.Fields.HEIGHT.ordinal(), container.tile.getPos(),
        0, TileHarvester.MAX_HEIGHT, container.tile.getField(f)));
    //
    f = TileHarvester.Fields.SIZE.ordinal();
    y += 28;
    size = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(), 0, TileHarvester.MAX_SIZE, container.tile.getField(f)));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    btnDirection.onValueUpdate(container.tile);
    heightslider.setTooltip("buildertype.height.tooltip");
    size.setTooltip("cyclic.screen.size" + container.tile.getField(size.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnDirection.visible = !container.tile.getBlockStateVertical();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
  }
}
