package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.FluidBar;
import com.lothrazar.library.gui.TexturedProgress;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenMelter extends ScreenBase<ContainerMelter> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TexturedProgress progress;
  private ButtonMachineField btnLock;

  public ScreenMelter(ContainerMelter screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    energy = new EnergyBar(this.font, TileMelter.MAX);
    fluid = new FluidBar(this.font, 132, 8, TileMelter.CAPACITY);
    this.progress = new TexturedProgress(this.font, 78, 32, 24, 17, TextureRegistry.ARROW);
    this.progress.setTopDown(false);
    progress.guiLeft = fluid.guiLeft = energy.guiLeft = leftPos;
    progress.guiTop = fluid.guiTop = energy.guiTop = topPos;
    btnLock = addRenderableWidget(new ButtonMachineField(leftPos + 80, topPos + 60,
        TileMelter.Fields.LOCK.ordinal(), menu.tile.getBlockPos(),
        TextureEnum.CRAFT_EMPTY, TextureEnum.CRAFT_BALANCE, "gui.cyclic.lock"));
    btnLock.setSize(14);
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnLock.onValueUpdate(menu.tile);
  }

  @Override
  protected void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
    this.progress.max = menu.tile.getField(TileMelter.Fields.BURNMAX.ordinal());
    progress.draw(ms,  menu.tile.getField(TileMelter.Fields.TIMER.ordinal()));
    fluid.draw(ms, menu.tile.getFluid());
    this.drawSlot(ms, 25, 30);
  }
}
