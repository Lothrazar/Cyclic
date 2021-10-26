package com.lothrazar.cyclic.block.anvilmagma;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenAnvilMagma extends ScreenBase<ContainerAnvilMagma> {

  private ButtonMachineField btnRedstone;
  private FluidBar fluid;

  public ScreenAnvilMagma(ContainerAnvilMagma screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 152, 8, TileAnvilMagma.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    fluid.guiLeft = guiLeft;
    fluid.guiTop = guiTop;
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileAnvilMagma.Fields.REDSTONE.ordinal(), container.tile.getPos()));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 54, 34);
    this.drawSlotLarge(ms, 104, 30);
    fluid.draw(ms, container.tile.getFluid());
  }
}
