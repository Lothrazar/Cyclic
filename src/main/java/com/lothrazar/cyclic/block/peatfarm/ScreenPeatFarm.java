package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.FluidBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPeatFarm extends ScreenBase<ContainerPeatFarm> {

  private EnergyBar energy;
  private FluidBar fluid;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

  public ScreenPeatFarm(ContainerPeatFarm screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    fluid = new FluidBar(this.font, 132, 8, TilePeatFarm.CAPACITY);
    energy = new EnergyBar(this.font, TilePeatFarm.MAX);
    fluid.guiLeft = energy.guiLeft = leftPos;
    fluid.guiTop = energy.guiTop = topPos;
    energy.visible = TilePeatFarm.POWERCONF.get() > 0;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TilePeatFarm.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    btnRender = addRenderableWidget(new ButtonMachineField(x, y + 20, TilePeatFarm.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
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
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
  }

  @Override
  protected void extractBackground(GuiGraphicsExtractor ms, int x, int y, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.getEnergy());
    fluid.draw(ms, menu.tile.getFluid());
    int rowSize = 6;
    int x1 = ContainerPeatFarm.SLOTX_START;
    int y1 = ContainerPeatFarm.SLOTY;
    for (int i = 0; i < rowSize; i++) {
      int x2 = x1 + i * Const.SQ - 1;
      int y2 = y1 - 1;
      this.drawSlot(ms, x2, y2);
    }
  }
}
