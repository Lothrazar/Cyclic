package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenHarvester extends ScreenBase<ContainerHarvester> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private GuiSliderInteger size;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger heightslider;

  public ScreenHarvester(ContainerHarvester screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileHarvester.MAX_ENERGY);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TileHarvester.POWERCONF.get() > 0;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileHarvester.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += 20;
    btnRender = addButton(new ButtonMachineField(x, y, TileHarvester.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //
    int f = TileHarvester.Fields.DIRECTION.ordinal();
    y += 20;
    btnDirection = addButton(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"))
    //.setSize(18)
    ;
    int w = 110;
    int h = 18;
    //now start sliders
    //
    y = topPos + 22;
    x = leftPos + 34;
    f = TileHarvester.Fields.HEIGHT.ordinal();
    heightslider = this.addButton(new GuiSliderInteger(x, y, w, h, TileHarvester.Fields.HEIGHT.ordinal(), menu.tile.getBlockPos(),
        0, TileHarvester.MAX_HEIGHT, menu.tile.getField(f)));
    heightslider.setTooltip("buildertype.height.tooltip");
    //     w = 130;
    //    int h = 18;
    f = TileHarvester.Fields.SIZE.ordinal();
    y += 26;
    size = this.addButton(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(), 0, TileHarvester.MAX_SIZE, menu.tile.getField(f)));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    btnDirection.onValueUpdate(menu.tile);
    size.setTooltip("cyclic.screen.size" + menu.tile.getField(size.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
