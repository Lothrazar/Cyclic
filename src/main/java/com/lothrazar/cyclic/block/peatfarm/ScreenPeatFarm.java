package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenPeatFarm extends ScreenBase<ContainerPeatFarm> {

  private EnergyBar energy;
  private FluidBar fluid;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

  public ScreenPeatFarm(ContainerPeatFarm screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 132, 8, TilePeatFarm.CAPACITY);
    energy = new EnergyBar(this, TilePeatFarm.MAX);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = energy.guiLeft = guiLeft;
    fluid.guiTop = energy.guiTop = guiTop;
    energy.visible = TilePeatFarm.POWERCONF.get() > 0;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TilePeatFarm.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    btnRender = addButton(new ButtonMachineField(x, y + 20, TilePeatFarm.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
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
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.getEnergy());
    fluid.draw(ms, container.tile.getFluid());
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
