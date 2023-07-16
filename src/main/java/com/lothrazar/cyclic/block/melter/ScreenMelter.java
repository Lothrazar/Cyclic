package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.FluidBar;
import com.lothrazar.library.gui.TexturedProgress;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenMelter extends ScreenBase<ContainerMelter> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TexturedProgress progress;

  public ScreenMelter(ContainerMelter screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    energy = new EnergyBar(this.font, TileMelter.MAX);
    fluid = new FluidBar(this.font, 132, 8, TileMelter.CAPACITY);
    this.progress = new TexturedProgress(this.font, 68, 37, 24, 17, TextureRegistry.ARROW);
    this.progress.setTopDown(false);
    progress.guiLeft = fluid.guiLeft = energy.guiLeft = leftPos;
    progress.guiTop = fluid.guiTop = energy.guiTop = topPos;
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
    this.drawName(ms, title.getString());
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
    this.progress.max = menu.tile.getField(TileMelter.Fields.BURNMAX.ordinal());
    progress.draw(ms, menu.tile.getField(TileMelter.Fields.TIMER.ordinal()));
    fluid.draw(ms, menu.tile.getFluid());
    this.drawSlot(ms, 16, 30);
    this.drawSlot(ms, 34, 30);
  }
}
