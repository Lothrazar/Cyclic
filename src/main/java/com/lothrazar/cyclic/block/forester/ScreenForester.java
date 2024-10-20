package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenForester extends ScreenBase<ContainerForester> {

  private ButtonMachineField btnRender;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private GuiSliderInteger size;
  private GuiSliderInteger heightslider;

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
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileForester.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    y += 20;
    btnRender = addButton(new ButtonMachineField(x, y, TileForester.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    final int w = 110;
    final int h = 18;
    int f = TileForester.Fields.HEIGHT.ordinal();
    x = guiLeft + 34;
    y = guiTop + 38;
    heightslider = this.addButton(new GuiSliderInteger(x, y, w, h, TileForester.Fields.HEIGHT.ordinal(), container.tile.getPos(),
        0, TileForester.MAX_HEIGHT, container.tile.getField(f)));
    //
    f = TileForester.Fields.SIZE.ordinal();
    //    x += 28;
    y += 20;
    size = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(), 0, TileForester.MAX_SIZE, container.tile.getField(f)));
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
    heightslider.setTooltip("buildertype.height.tooltip");
    size.setTooltip("cyclic.screen.size" + container.tile.getField(size.getField()));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    int relX = this.getXSize() / 2 - 9;
    int y = 16;
    //
    if (container.tile.hasSapling()) {
      this.drawSlot(ms, relX, y);
    }
    else {
      this.drawSlot(ms, relX, y, TextureRegistry.SLOT_SAPLING, Const.SQ);
    }
    energy.draw(ms, container.getEnergy());
  }
}
