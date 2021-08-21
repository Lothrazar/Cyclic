package com.lothrazar.cyclic.block.anvilvoid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenAnvilVoid extends ScreenBase<ContainerAnvilVoid> {

  private ButtonMachineRedstone btnRedstone;
  private FluidBar fluid;

  public ScreenAnvilVoid(ContainerAnvilVoid screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 152, 8, TileAnvilVoid.CAPACITY);
    fluid.emtpyTooltip = "0 " + UtilChat.lang("fluid.cyclic.xpjuice");
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = guiLeft;
    fluid.guiTop = guiTop;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileAnvilVoid.Fields.REDSTONE.ordinal(), container.tile.getPos()));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    btnRedstone.onValueUpdate(container.tile);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 54, 34);
    this.drawSlotLarge(ms, 104, 30);
    fluid.draw(ms, container.tile.getFluid());
  }
}
