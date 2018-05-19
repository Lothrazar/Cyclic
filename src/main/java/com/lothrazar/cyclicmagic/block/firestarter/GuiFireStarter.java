package com.lothrazar.cyclicmagic.block.firestarter;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFireStarter extends GuiBaseContainer {

  public GuiFireStarter(InventoryPlayer inventoryPlayer, TileEntityFireStarter tile) {
    super(new ContainerFireStarter(inventoryPlayer, tile), tile);
    this.fieldRedstoneBtn = TileEntityFireStarter.Fields.REDSTONE.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setWidth(16).setX(this.xSize - 26);
  }

  @Override
  public void initGui() {
    super.initGui();
    int h = 12;
    int x = this.guiLeft + 16;
    int y = this.guiTop + 36;
    int id = 1;
    GuiSliderInteger sliderOffset = new GuiSliderInteger(tile, id++, x, y, 120, h, 0, 16,
        TileEntityFireStarter.Fields.OFFSET.ordinal());
    sliderOffset.setTooltip("fire_starter.offset.tooltip");
    this.addButton(sliderOffset);
    y += 18;
  }
}
