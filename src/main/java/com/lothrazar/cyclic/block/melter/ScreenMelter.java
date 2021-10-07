package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenMelter extends ScreenBase<ContainerMelter> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TimerBar timer;

  public ScreenMelter(ContainerMelter screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    energy = new EnergyBar(this, TileMelter.MAX);
    fluid = new FluidBar(this, 132, 8, TileMelter.CAPACITY);
    timer = new TimerBar(this, 88, 37, TileMelter.TIMER_FULL);
  }

  @Override
  public void init() {
    super.init();
    timer.guiLeft = fluid.guiLeft = energy.guiLeft = leftPos;
    timer.guiTop = fluid.guiTop = energy.guiTop = topPos;
    energy.visible = (TileMelter.POWERCONF.get() > 0);
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
    timer.draw(ms, menu.tile.getField(TileMelter.Fields.TIMER.ordinal()));
    fluid.draw(ms, menu.tile.getFluid());
    this.drawSlot(ms, 16, 30);
    this.drawSlot(ms, 34, 30);
  }
}
