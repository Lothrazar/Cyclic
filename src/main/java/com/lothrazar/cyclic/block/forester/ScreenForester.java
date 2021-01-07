package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenForester extends ScreenBase<ContainerForester> {

  private ButtonMachineRedstone btnRender;
  private ButtonMachineRedstone btnRedstone;
  private EnergyBar energy;
  private GuiSliderInteger size;

  public ScreenForester(ContainerForester screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileForester.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    energy.visible = TileForester.POWERCONF.get() > 0;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileForester.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    y += 20;
    btnRender = addButton(new ButtonMachineRedstone(x, y, TileForester.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    int w = 110;
    int h = 18;
    int f = TileForester.Fields.SIZE.ordinal();
    x += 28;
    y += 20;
    size = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(), 0, 10, container.tile.getField(f)));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    size.setTooltip("cyclic.screen.size" + container.tile.getField(size.getField()));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    int relX = this.getXSize() / 2 - 9;
    this.drawSlot(ms, relX, 24, TextureRegistry.SLOTSAPLING, Const.SQ);
    energy.draw(ms, container.getEnergy());
  }
}
