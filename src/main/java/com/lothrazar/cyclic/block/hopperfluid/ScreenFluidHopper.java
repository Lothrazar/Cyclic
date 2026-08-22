package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.FluidBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
    fluid.guiLeft = leftPos;
    fluid.guiTop = topPos;


  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawName(ms, this.title.getString());
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 79, 23, TextureRegistry.SLOT_FILTER_FLUID, 18);
    fluid.draw(ms, menu.tile.getFluid());
  }
}
