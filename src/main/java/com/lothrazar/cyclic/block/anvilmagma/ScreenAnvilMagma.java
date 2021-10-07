package com.lothrazar.cyclic.block.anvilmagma;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenAnvilMagma extends ScreenBase<ContainerAnvilMagma> {

  private ButtonMachineField btnRedstone;
  private FluidBar fluid;

  public ScreenAnvilMagma(ContainerAnvilMagma screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 152, 8, TileAnvilMagma.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    fluid.guiLeft = leftPos;
    fluid.guiTop = topPos;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileAnvilMagma.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
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
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 54, 34);
    this.drawSlotLarge(ms, 104, 30);
    fluid.draw(ms, menu.tile.getFluid());
  }
}
