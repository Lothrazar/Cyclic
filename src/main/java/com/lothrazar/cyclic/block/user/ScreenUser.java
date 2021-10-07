package com.lothrazar.cyclic.block.user;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenUser extends ScreenBase<ContainerUser> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;

  public ScreenUser(ContainerUser screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileUser.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.visible = TileUser.POWERCONF.get() > 0;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x, y;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addWidget(new ButtonMachineField(x, y, TileUser.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    //
    x = leftPos + 32;
    y = topPos + 26;
    int w = 120;
    int h = 20;
    int f = TileUser.Fields.TIMERDEL.ordinal();
    GuiSliderInteger slider = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 64, menu.tile.getField(f)));
    slider.setTooltip("block.cyclic.user.delay");
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
    btnRedstone.onValueUpdate(menu.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 9, 34);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
