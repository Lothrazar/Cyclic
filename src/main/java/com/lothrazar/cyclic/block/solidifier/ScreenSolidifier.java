package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenSolidifier extends ScreenBase<ContainerSolidifier> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TimerBar timer;

  public ScreenSolidifier(ContainerSolidifier screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileSolidifier.MAX);
    fluid = new FluidBar(this, 8, 8, TileSolidifier.CAPACITY);
    timer = new TimerBar(this, 68, 37, TileSolidifier.TIMER_FULL);
  }

  @Override
  public void init() {
    super.init();
    timer.guiLeft = fluid.guiLeft = energy.guiLeft = guiLeft;
    timer.guiTop = fluid.guiTop = energy.guiTop = guiTop;
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
    //    this.drawName(this.title.getFormattedText());
    int timer = container.tile.getField(TileSolidifier.Fields.TIMER.ordinal());
    if (timer > 0)
      this.font.drawString("[" + timer + "]",
          (this.getXSize()) / 2 - 16,
          40.0F, 4209792);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(36, 12);
    this.drawSlot(36, 30);
    this.drawSlot(36, 48);
    timer.draw(container.tile.getField(TileSolidifier.Fields.TIMER.ordinal()));
    energy.draw(container.getEnergy());
    fluid.draw(container.tile.getFluid());
    drawSlotLarge(116, 26);
  }
}
