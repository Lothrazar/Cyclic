package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.FluidBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFluidHopper extends ScreenBase<ContainerFluidHopper> {

  private FluidBar fluid;
  public ScreenFluidHopper(ContainerFluidHopper screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    fluid = new FluidBar(this.font, 8, 8, TileFluidHopper.CAPACITY);
  fluid.guiLeft =  leftPos;
  fluid.guiTop =   topPos;


  }
  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms, mouseX, mouseY, partialTicks);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 79, 23, TextureRegistry.SLOT_FILTER_FLUID, 18);
    fluid.draw(ms, menu.tile.getFluid());
  }
}
