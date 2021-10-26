package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenDisenchant extends ScreenBase<ContainerDisenchant> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private FluidBar fluid;

  public ScreenDisenchant(ContainerDisenchant screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    energy = new EnergyBar(this, TileDisenchant.MAX);
    fluid = new FluidBar(this, 134, 8, TileDisenchant.CAPACITY);
    fluid.emtpyTooltip = "0 " + UtilChat.lang("fluid.cyclic.xpjuice");
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = energy.guiLeft = guiLeft;
    fluid.guiTop = energy.guiTop = guiTop;
    int x, y;
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileDisenchant.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    energy.visible = TileDisenchant.POWERCONF.get() > 0;
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
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
    int x = 23;
    int y = 39;
    this.drawSlot(ms, x, y);
    this.drawSlot(ms, x + 24, y, TextureRegistry.SLOT_BOOK, 18);
    this.drawSlotLarge(ms, 103, y - 20);
    this.drawSlotLarge(ms, 103, y + 12);
    energy.draw(ms, container.tile.getEnergy());
    fluid.draw(ms, container.tile.getFluid());
  }
}
