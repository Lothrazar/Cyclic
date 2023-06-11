package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDisenchant extends ScreenBase<ContainerDisenchant> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private FluidBar fluid;

  public ScreenDisenchant(ContainerDisenchant screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    energy = new EnergyBar(this.font, TileDisenchant.MAX);
    fluid = new FluidBar(this.font, 134, 8, TileDisenchant.CAPACITY);
    fluid.emtpyTooltip = "0 " + ChatUtil.lang("fluid.cyclic.xpjuice");
    fluid.guiLeft = energy.guiLeft = leftPos;
    fluid.guiTop = energy.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileDisenchant.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    energy.visible = TileDisenchant.POWERCONF.get() > 0;
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    int x = 23;
    int y = 39;
    this.drawSlot(ms, x, y);
    this.drawSlot(ms, x + 24, y, TextureRegistry.SLOT_BOOK, 18);
    this.drawSlotLarge(ms, 103, y - 20);
    this.drawSlotLarge(ms, 103, y + 12);
    energy.draw(ms, menu.tile.getEnergy());
    fluid.draw(ms, menu.tile.getFluid());
  }
}
