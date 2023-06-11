package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPeatFarm extends ScreenBase<ContainerPeatFarm> {

  private EnergyBar energy;
  private FluidBar fluid;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

  public ScreenPeatFarm(ContainerPeatFarm screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this.font, 132, 8, TilePeatFarm.CAPACITY);
    energy = new EnergyBar(this.font, TilePeatFarm.MAX);
  }

  @Override
  public void init() {
    super.init();
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
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int x, int y) {
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
