package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenItemCollector extends ScreenBase<ContainerItemCollector> {

  private ButtonMachineRedstone btnRedstone;
  private ButtonMachineRedstone btnRender;
  private GuiSliderInteger sizeSlider;
  private ButtonMachineRedstone btnDirection;

  public ScreenItemCollector(ContainerItemCollector screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);;
    this.ySize = 256;
  }

  @Override
  public void init() {
    super.init();
    int x = guiLeft + 8;
    int y = guiTop + 8;
    int f = TileItemCollector.Fields.REDSTONE.ordinal();
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, f, container.tile.getPos()));
    f = TileItemCollector.Fields.RENDER.ordinal();
    y += 20;
    btnRender = addButton(new ButtonMachineRedstone(x, y, f,
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"))
    //            .setSize(18)
    ;
    //then toggle
    f = TileItemCollector.Fields.DIRECTION.ordinal();
    y += 20;
    btnDirection = addButton(new ButtonMachineRedstone(x, y, f,
        container.tile.getPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"))
    //.setSize(18)
    ;
    int w = 110;
    int h = 18;
    //now start sliders
    //
    y = guiTop + 22;
    x = guiLeft + 34;
    f = TileItemCollector.Fields.HEIGHT.ordinal();
    GuiSliderInteger heightslider = this.addButton(new GuiSliderInteger(x, y, w, h, TileItemCollector.Fields.HEIGHT.ordinal(), container.tile.getPos(),
        0, TileItemCollector.MAX_HEIGHT, container.tile.getField(f)));
    heightslider.setTooltip("buildertype.height.tooltip");
    //then size
    f = TileItemCollector.Fields.SIZE.ordinal();
    y += h + 1;
    sizeSlider = this.addButton(new GuiSliderInteger(x, y, w, h, TileItemCollector.Fields.SIZE.ordinal(),
        container.tile.getPos(), 0, TileItemCollector.MAX_SIZE, container.tile.getField(f)));
    sizeSlider.setTooltip("buildertype.size.tooltip");
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    btnDirection.onValueUpdate(container.tile);
    sizeSlider.setTooltip("cyclic.screen.size" + container.tile.getField(sizeSlider.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    for (int i = 0; i < 9; i++) {
      int y = 81;
      this.drawSlot(ms, 7 + i * Const.SQ, y);
      this.drawSlot(ms, 7 + i * Const.SQ, y + Const.SQ);
    }
  }
}
