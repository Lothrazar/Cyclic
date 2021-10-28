package com.lothrazar.cyclic.block.placerfluid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPlacerFluid extends ScreenBase<ContainerPlacerFluid> {

  private FluidBar fluid;
  private ButtonMachineField btnRedstone;

  public ScreenPlacerFluid(ContainerPlacerFluid screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 152, 14, TilePlacerFluid.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = leftPos;
    fluid.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TilePlacerFluid.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getFluid());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    fluid.draw(ms, menu.tile.getFluid());
  }
}
