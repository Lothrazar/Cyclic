package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.FluidBar;
import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDisenchant extends ScreenBase<ContainerDisenchant> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private FluidBar fluid;
  private TexturedProgress progress;

  public ScreenDisenchant(ContainerDisenchant screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    energy = new EnergyBar(this.font, TileDisenchant.MAX);
    fluid = new FluidBar(this.font, 134, 8, TileDisenchant.CAPACITY);
    fluid.emtpyTooltip = "0 " + ChatUtil.lang("fluid_type.cyclic.xpjuice");
    progress = new TexturedProgress(this.font, 69, 40, 24, 17, TextureRegistry.ARROW);
    progress.setTopDown(false);
    fluid.guiLeft = energy.guiLeft = progress.guiLeft = leftPos;
    fluid.guiTop = energy.guiTop = progress.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileDisenchant.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    energy.visible = TileDisenchant.POWERCONF.get() > 0;
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
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    int x = 23;
    int y = 39;
    this.drawSlot(ms, x, y);
    this.drawSlot(ms, x + 24, y, TextureRegistry.SLOT_BOOK, 18);
    this.drawSlotLarge(ms, 103, y - 20);
    this.drawSlotLarge(ms, 103, y + 12);
    energy.draw(ms, menu.tile.getEnergy());
    fluid.draw(ms, menu.tile.getFluid());
    final int timerMax = menu.tile.getField(TileDisenchant.Fields.TIMERMAX.ordinal());
    progress.max = timerMax;
    progress.draw(ms, timerMax - menu.tile.getField(TileDisenchant.Fields.TIMER.ordinal()));
  }
}
