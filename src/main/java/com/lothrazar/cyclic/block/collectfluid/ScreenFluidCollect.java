package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFluidCollect extends ScreenBase<ContainerFluidCollect> {

  private EnergyBar energy;
  private FluidBar fluid;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

  public ScreenFluidCollect(ContainerFluidCollect screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this.font, TileFluidCollect.CAPACITY);
    energy = new EnergyBar(this.font, TileFluidCollect.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = fluid.guiLeft = leftPos;
    energy.guiTop = fluid.guiTop = topPos;
    energy.visible = TileFluidCollect.POWERCONF.get() > 0;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileFluidCollect.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    btnRender = addRenderableWidget(new ButtonMachineField(x, y + 20, TileFluidCollect.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //
    int w = 96;
    int h = 20;
    x = leftPos + 32;
    y += h + 1;
    int f = TileFluidCollect.Fields.HEIGHT.ordinal();
    GuiSliderInteger height = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, TileFluidCollect.MAX_HEIGHT, menu.tile.getField(f)));
    height.setTooltip("buildertype.height.tooltip");
    y += h + 1;
    //
    //
    f = TileFluidCollect.Fields.SIZE.ordinal();
    GuiSliderInteger size = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, TileMiner.MAX_SIZE, menu.tile.getField(f)));
    size.setTooltip("buildertype.size.tooltip");
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    //    int on = container.tile.getField(TileFluidCollect.Fields.RENDER.ordinal());
    //    btnRender.setTooltip(UtilChat.lang("gui.cyclic.render" + on));
    //    btnRender.setTextureId(on == 1 ? TextureEnum.RENDER_SHOW : TextureEnum.RENDER_HIDE);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 9, 50);
    if (TileFluidCollect.POWERCONF.get() > 0) {
      energy.draw(ms, menu.tile.getEnergy());
    }
    fluid.draw(ms, menu.tile.getFluid());
  }
}
