package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenForester extends ScreenBase<ContainerForester> {

  private ButtonMachineField btnRender;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private GuiSliderInteger size;

  public ScreenForester(ContainerForester screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileForester.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TileForester.POWERCONF.get() > 0;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileForester.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += 20;
    btnRender = addButton(new ButtonMachineField(x, y, TileForester.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    int w = 110;
    int h = 18;
    int f = TileForester.Fields.SIZE.ordinal();
    x += 28;
    y += 20;
    size = this.addButton(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(), 0, 10, menu.tile.getField(f)));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    size.setTooltip("cyclic.screen.size" + menu.tile.getField(size.getField()));
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    int relX = this.getXSize() / 2 - 9;
    this.drawSlot(ms, relX, 24, TextureRegistry.SLOT_SAPLING, Const.SQ);
    energy.draw(ms, menu.getEnergy());
  }
}
