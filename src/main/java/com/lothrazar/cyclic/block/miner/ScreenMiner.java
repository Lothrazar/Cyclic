package com.lothrazar.cyclic.block.miner;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenMiner extends ScreenBase<ContainerMiner> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private EnergyBar energy;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger sizeSlider;

  public ScreenMiner(ContainerMiner screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileMiner.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TileMiner.POWERCONF.get() > 0;
    int x = leftPos + 8;
    int y = topPos + 8;
    int f = TileMiner.Fields.REDSTONE.ordinal();
    btnRedstone = addWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    f = TileMiner.Fields.RENDER.ordinal();
    btnRender = addWidget(new ButtonMachineField(x, y + 20, f,
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //then toggle
    f = TileMiner.Fields.DIRECTION.ordinal();
    btnDirection = addWidget(new ButtonMachineField(x, y + 40, f,
        menu.tile.getBlockPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"));
    //
    int w = 120;
    int h = 20;
    x = leftPos + 32;
    y += h + 1;
    // height fi
    f = TileMiner.Fields.HEIGHT.ordinal();
    GuiSliderInteger heightslider = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, TileMiner.MAX_HEIGHT, menu.tile.getField(f)));
    heightslider.setTooltip("buildertype.height.tooltip");
    y += h + 1;
    //
    f = TileMiner.Fields.SIZE.ordinal();
    sizeSlider = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, TileMiner.MAX_SIZE, menu.tile.getField(f)));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    btnDirection.onValueUpdate(menu.tile);
    sizeSlider.setTooltip("cyclic.screen.size" + menu.tile.getField(sizeSlider.getField()));
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    if (menu.tile.inventory.getStackInSlot(TileMiner.SLOT_TOOL).isEmpty()) {
      this.drawSlot(ms, 32, 8, TextureRegistry.SLOT_TOOL, 18);
    }
    else {
      this.drawSlot(ms, 32, 8);
    }
    this.drawSlot(ms, 134, 8, TextureRegistry.SLOT_BSDATA, 18);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
