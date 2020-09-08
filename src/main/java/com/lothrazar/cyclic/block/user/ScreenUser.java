package com.lothrazar.cyclic.block.user;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenUser extends ScreenBase<ContainerUser> {

  private EnergyBar energy;
  private ButtonMachineRedstone btnRedstone;

  public ScreenUser(ContainerUser screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileUser.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileUser.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    //
    x = guiLeft + 32;
    y = guiTop + 26;
    int w = 120;
    int h = 20;
    int f = TileUser.Fields.TIMERDEL.ordinal();
    GuiSliderInteger DROPCOUNT = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, 64, container.tile.getField(f)));
    DROPCOUNT.setTooltip("block.cyclic.user.delay");
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
    this.drawSlot(ms, 9, 34);
  }
}
