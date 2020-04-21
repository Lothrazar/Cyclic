package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenSolidifier extends ScreenBase<ContainerSolidifier> {

  private EnergyBar energy;
  private FluidBar fluid;

  public ScreenSolidifier(ContainerSolidifier screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileSolidifier.MAX);
    fluid = new FluidBar(this, 8, 8, TileSolidifier.CAPACITY);
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
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.GUI);
    this.drawSlot(36, 30);
    this.drawSlot(126, 30);
    energy.draw(container.getEnergy());
    fluid.draw(this.container.tile.getFluid());
  }
}
