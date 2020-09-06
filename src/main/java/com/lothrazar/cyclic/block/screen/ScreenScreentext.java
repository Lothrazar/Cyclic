package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenScreentext extends ScreenBase<ContainerScreentext> {

  private TextBoxAutosave txtString;

  public ScreenScreentext(ContainerScreentext screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    int w = 160;
    x = guiLeft + 8;
    y = guiTop + 8;
    txtString = new TextBoxAutosave(this.font, x, y, w, container.tile.getPos(), 0);
    txtString.setText(container.tile.getFieldString(0));
    this.children.add(txtString);
    //test/
    int f = TileScreentext.Fields.RED.ordinal();
    x = guiLeft + 8;
    y = guiTop + 28;
    int h = 20;
    this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 256, container.tile.getField(f)));
    f = TileScreentext.Fields.GREEN.ordinal();
    y += h;
    this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 256, container.tile.getField(f)));
    f = TileScreentext.Fields.BLUE.ordinal();
    y += h;
    this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 256, container.tile.getField(f)));
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
    //    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.txtString.render(ms, mouseX, mouseY, partialTicks);
    //  energy.draw(ms, container.getEnergy());
  }
}
