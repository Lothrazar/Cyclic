package com.lothrazar.cyclic.block.anvil;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenAnvil extends ScreenBase<ContainerAnvil> {

  private ButtonMachineField btnRedstone;
  private EnergyBar energy;

  public ScreenAnvil(ContainerAnvil screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileAnvilAuto.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.visible = TileAnvilAuto.POWERCONF.get() > 0;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x, y;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileAnvilAuto.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
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
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 54, 34);
    this.drawSlotLarge(ms, 104, 30);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
