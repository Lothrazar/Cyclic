package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDropper extends ScreenBase<ContainerDropper> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

  public ScreenDropper(ContainerDropper screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileDropper.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y, w, h;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TileDropper.POWERCONF.get() > 0;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileDropper.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += 20;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, TileDropper.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x = leftPos + 32;
    y = topPos + 18;
    w = 120;
    h = 16;
    int f = TileDropper.Fields.DROPCOUNT.ordinal();
    GuiSliderInteger dropcount = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 64, menu.tile.getField(f)));
    dropcount.setTooltip("cyclic.dropper.count");
    y += h + 1;
    f = TileDropper.Fields.DELAY.ordinal();
    GuiSliderInteger delaysli = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 500, menu.tile.getField(f)));
    delaysli.setTooltip("cyclic.dropper.delay");
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRender.onValueUpdate(menu.tile);
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 88, 58);
    this.drawSlot(ms, 9, 58, TextureRegistry.SLOT_GPS);
    energy.draw(ms, menu.getEnergy());
  }
}
