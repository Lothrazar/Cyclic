package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenSolidifier extends ScreenBase<ContainerSolidifier> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TexturedProgress progress;

  public ScreenSolidifier(ContainerSolidifier screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileSolidifier.MAX);
    fluid = new FluidBar(this, 8, 8, TileSolidifier.CAPACITY);
    this.progress = new TexturedProgress(this, 68, 37, 24, 17, TextureRegistry.ARROW);
    this.progress.setTopDown(false);
    this.progress.max = TileSolidifier.TIMER_FULL;
  }

  @Override
  public void init() {
    super.init();
    progress.guiLeft = fluid.guiLeft = energy.guiLeft = guiLeft;
    progress.guiTop = fluid.guiTop = energy.guiTop = guiTop;
    energy.visible = TileSolidifier.POWERCONF.get() > 0;
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.getEnergy());
    progress.draw(ms, container.tile.getField(TileSolidifier.Fields.TIMER.ordinal()));
    fluid.draw(ms, container.tile.getFluid());
    drawSlotLarge(ms, 116, 32);
    this.drawSlot(ms, 36, 16);
    this.drawSlot(ms, 36, 34);
    this.drawSlot(ms, 36, 52);
  }
}
