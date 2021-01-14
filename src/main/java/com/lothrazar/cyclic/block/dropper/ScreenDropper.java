package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenDropper extends ScreenBase<ContainerDropper> {

  private EnergyBar energy;
  private ButtonMachineRedstone btnRedstone;
  private ButtonMachineRedstone btnRender;

  public ScreenDropper(ContainerDropper screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileDropper.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y, w, h;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    energy.visible = TileDropper.POWERCONF.get() > 0;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileDropper.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    y += 20;
    btnRender = addButton(new ButtonMachineRedstone(x, y, TileDropper.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x = guiLeft + 32;
    y = guiTop + 18;
    w = 120;
    h = 20;
    int f = TileDropper.Fields.DROPCOUNT.ordinal();
    GuiSliderInteger dropcount = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, 64, container.tile.getField(f)));
    dropcount.setTooltip("cyclic.dropper.count");
    y += h + 1;
    f = TileDropper.Fields.OFFSET.ordinal();
    GuiSliderInteger offsetsli = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 16, container.tile.getField(f)));
    offsetsli.setTooltip("cyclic.dropper.offset");
    y += h + 1;
    f = TileDropper.Fields.DELAY.ordinal();
    GuiSliderInteger delaysli = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, 500, container.tile.getField(f)));
    delaysli.setTooltip("cyclic.dropper.delay");
    //    y += 22;
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRender.onValueUpdate(container.tile);
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 9, 50);
    energy.draw(ms, container.getEnergy());
  }
}
