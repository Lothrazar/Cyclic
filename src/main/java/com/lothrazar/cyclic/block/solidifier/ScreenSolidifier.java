package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenSolidifier extends ScreenBase<ContainerSolidifier> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TexturedProgress progress;

  public ScreenSolidifier(ContainerSolidifier screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileSolidifier.MAX);
    fluid = new FluidBar(this, 8, 8, TileSolidifier.CAPACITY);
    this.progress = new TexturedProgress(this, 68, 37, 24, 17, TextureRegistry.ARROW);
    this.progress.setTopDown(false);
  }

  @Override
  public void init() {
    super.init();
    progress.guiLeft = fluid.guiLeft = energy.guiLeft = leftPos;
    progress.guiTop = fluid.guiTop = energy.guiTop = topPos;
    //    energy.visible = TileSolidifier.POWERCONF.get() > 0;
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
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
    energy.draw(ms, menu.getEnergy());
    this.progress.max = menu.tile.getField(TileSolidifier.Fields.BURNMAX.ordinal());
    progress.draw(ms, menu.tile.getField(TileSolidifier.Fields.TIMER.ordinal()));
    fluid.draw(ms, menu.tile.getFluid());
    drawSlotLarge(ms, 116, 32);
    this.drawSlot(ms, 36, 16);
    this.drawSlot(ms, 36, 34);
    this.drawSlot(ms, 36, 52);
  }
}
