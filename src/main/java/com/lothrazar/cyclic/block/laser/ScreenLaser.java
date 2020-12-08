package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenLaser extends ScreenBase<ContainerLaser> {

  private ButtonMachineRedstone btnRedstone;

  public ScreenLaser(ContainerLaser screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.ySize = 256;
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileLaser.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    //
    y += 20;
    int w = 160;
    int h = 10;
    int f = TileLaser.Fields.RED.ordinal();
    GuiSliderInteger red = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 255, container.tile.getField(f)));
    red.setTooltip("cyclic.screen.red");
    y += h + 1;
    f = TileLaser.Fields.GREEN.ordinal();
    GuiSliderInteger green = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 255, container.tile.getField(f)));
    green.setTooltip("cyclic.screen.green");
    y += h + 1;
    f = TileLaser.Fields.BLUE.ordinal();
    GuiSliderInteger blue = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 255, container.tile.getField(f)));
    blue.setTooltip("cyclic.screen.blue");
    y += h + 1;
    f = TileLaser.Fields.ALPHA.ordinal();
    GuiSliderInteger alpha = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, 100, container.tile.getField(f)));
    alpha.setTooltip("cyclic.screen.alpha");
    y += h + 1;
    f = TileLaser.Fields.THICK.ordinal();
    GuiSliderInteger thick = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, 20, container.tile.getField(f)));
    thick.setTooltip("cyclic.screen.thick");
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    this.drawSlot(ms, 151, 7, TextureRegistry.SLOT_GPS, 18);
  }
}
