package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.melter.TileMelter.Fields;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenMelter extends ScreenBase<ContainerMelter> {

  private EnergyBar energy;
  private FluidBar fluid;

  public ScreenMelter(ContainerMelter screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    energy = new EnergyBar(this, TileMelter.MAX);
    fluid = new FluidBar(this, 132, 8, TileMelter.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = energy.guiLeft = guiLeft;
    fluid.guiTop = energy.guiTop = guiTop;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    energy.renderHoveredToolTip(mouseX, mouseY, container.getEnergy());
    fluid.renderHoveredToolTip(mouseX, mouseY, container.tile.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawButtonTooltips(mouseX, mouseY);
    this.drawName(this.title.getFormattedText());
    int timer = container.tile.getField(Fields.TIMER.ordinal());
    if (timer > 0)
      this.font.drawString("[" + timer + "]",
          (this.getXSize()) / 2 + 4,
          40.0F, 4209792);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(16, 30);
    this.drawSlot(34, 30);
    energy.draw(container.getEnergy());
    fluid.draw(this.container.tile.getFluid());
  }
}
