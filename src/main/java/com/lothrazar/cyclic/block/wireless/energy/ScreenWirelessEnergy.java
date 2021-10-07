package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenWirelessEnergy extends ScreenBase<ContainerWirelessEnergy> {

  private ButtonMachineField btnRedstone;
  private EnergyBar energy;

  public ScreenWirelessEnergy(ContainerWirelessEnergy screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileWirelessEnergy.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x, y;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addWidget(new ButtonMachineField(x, y, TileWirelessEnergy.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 79, 35, TextureRegistry.SLOT_GPS);
  }
}
