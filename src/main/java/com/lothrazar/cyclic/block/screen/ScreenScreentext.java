package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenScreentext extends ScreenBase<ContainerScreentext> {

  private ButtonMachineRedstone btnRedstone;
  private TextBoxAutosave txtString;

  public ScreenScreentext(ContainerScreentext screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x = guiLeft + 6;
    int y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileScreentext.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    int w = 140;
    x = guiLeft + 28;
    y = guiTop + 8;
    txtString = new TextBoxAutosave(this.font, x, y, w, container.tile.getPos(), 0);
    txtString.setText(container.tile.getFieldString(0));
    this.children.add(txtString);
    w = 160;
    x = guiLeft + 8;
    y = guiTop + 28;
    int h = 20;
    int f = TileScreentext.Fields.RED.ordinal();
    GuiSliderInteger red = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 255, container.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.GREEN.ordinal();
    GuiSliderInteger green = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 255, container.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.BLUE.ordinal();
    GuiSliderInteger blue = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 255, container.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.PADDING.ordinal();
    GuiSliderInteger pad = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 20, container.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.FONT.ordinal();
    GuiSliderInteger font = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, 100, container.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.OFFSET.ordinal();
    GuiSliderInteger offset = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 10, container.tile.getField(f)));
    red.setTooltip("cyclic.screen.red");
    green.setTooltip("cyclic.screen.green");
    blue.setTooltip("cyclic.screen.blue");
    font.setTooltip("cyclic.screen.font");
    offset.setTooltip("cyclic.screen.offset");
    pad.setTooltip("cyclic.screen.padding");
  }

  @Override
  public void tick() {
    this.txtString.tick();
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
    //energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_PLAIN);
    this.txtString.render(ms, mouseX, mouseY, partialTicks);
  }
}
