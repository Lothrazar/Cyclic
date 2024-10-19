package com.lothrazar.cyclic.block.miner;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenMiner extends ScreenBase<ContainerMiner> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private EnergyBar energy;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger sizeSlider;

  public ScreenMiner(ContainerMiner screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileMiner.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    energy.visible = TileMiner.POWERCONF.get() > 0;
    int x = guiLeft + 6;
    int y = guiTop + 6;
    int f = TileMiner.Fields.REDSTONE.ordinal();
    btnRedstone = addButton(new ButtonMachineField(x, y, f, container.tile.getPos()));
    f = TileMiner.Fields.RENDER.ordinal();
    btnRender = addButton(new ButtonMachineField(x, y + 20, f,
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //then toggle
    f = TileMiner.Fields.DIRECTION.ordinal();
    btnDirection = addButton(new ButtonMachineField(x, y + 40, f,
        container.tile.getPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"));
    btnDirection.visible = !container.tile.getBlockStateVertical();
    //
    int w = 120;
    int h = 20;
    x = guiLeft + 32;
    y += h + 1;
    // height fi
    f = TileMiner.Fields.HEIGHT.ordinal();
    GuiSliderInteger heightslider = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, TileMiner.MAX_HEIGHT, container.tile.getField(f)));
    heightslider.setTooltip("buildertype.height.tooltip");
    y += h + 1;
    //
    f = TileMiner.Fields.SIZE.ordinal();
    sizeSlider = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, TileMiner.MAX_SIZE, container.tile.getField(f)));
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
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    btnDirection.onValueUpdate(container.tile);
    sizeSlider.setTooltip("cyclic.screen.size" + container.tile.getField(sizeSlider.getField()));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    if (container.tile.inventory.getStackInSlot(TileMiner.SLOT_TOOL).isEmpty()) {
      this.drawSlot(ms, 32, 8, TextureRegistry.SLOT_TOOL, 18);
    }
    else {
      this.drawSlot(ms, 32, 8);
    }
    this.drawSlot(ms, 134, 8, TextureRegistry.SLOT_BSDATA, 18);
    energy.draw(ms, container.tile.getEnergy());
  }
}
