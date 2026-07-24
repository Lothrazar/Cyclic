package com.lothrazar.cyclic.block.solidifier;

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

public class ScreenSolidifier extends ScreenBase<ContainerSolidifier> {

  private EnergyBar energy;
  private FluidBar fluid;
  private TexturedProgress progress;
  private ButtonMachineField btnLock;

  public ScreenSolidifier(ContainerSolidifier screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileSolidifier.MAX);
    fluid = new FluidBar(this.font, 8, 8, TileSolidifier.CAPACITY);
    this.progress = new TexturedProgress(this.font, 68, 37, 24, 17, TextureRegistry.ARROW);
    this.progress.setTopDown(false);
    progress.guiLeft = fluid.guiLeft = energy.guiLeft = leftPos;
    progress.guiTop = fluid.guiTop = energy.guiTop = topPos;
    //    energy.visible = TileSolidifier.POWERCONF.get() > 0;
    btnLock = addRenderableWidget(new ButtonMachineField(leftPos + 80, topPos + 60,
        TileSolidifier.Fields.LOCK.ordinal(), menu.tile.getBlockPos(),
        TextureEnum.CRAFT_EMPTY, TextureEnum.CRAFT_BALANCE, "gui.cyclic.lock"));
    btnLock.setSize(14);
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
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
