package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCableFluid extends ScreenBase<ContainerCableFluid> {

  public ScreenCableFluid(ContainerCableFluid screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    //    fluid = new FluidBar(this, TileFluidCollect.CAPACITY);
    //    energy = new EnergyBar(this, TileFluidCollect.MAX);
  }

  @Override
  public void init() {
    super.init();
    //    energy.guiLeft = fluid.guiLeft = guiLeft;
    //    energy.guiTop = fluid.guiTop = guiTop;
    //    energy.visible = TileFluidCollect.POWERCONF.get() > 0;
    //    int x, y;
    //    x = guiLeft + 8;
    //    y = guiTop + 8;
    //    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileFluidCollect.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    //    btnRender = addButton(new ButtonMachineRedstone(x, y + 20, TileFluidCollect.Fields.RENDER.ordinal(),
    //        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //    //
    //    //
    //    int w = 96;
    //    int h = 20;
    //    x = guiLeft + 32;
    //    y += h + 1;
    //    int f = TileFluidCollect.Fields.HEIGHT.ordinal();
    //    GuiSliderInteger height = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
    //        0, TileFluidCollect.MAX_HEIGHT, container.tile.getField(f)));
    //    height.setTooltip("buildertype.height.tooltip");
    //    y += h + 1;
    //    //
    //    //
    //    f = TileFluidCollect.Fields.SIZE.ordinal();
    //    GuiSliderInteger size = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
    //        0, TileMiner.MAX_SIZE, container.tile.getField(f)));
    //    size.setTooltip("buildertype.size.tooltip");
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, imageWidth / 2 - 9, 28, TextureRegistry.SLOT_FILTER, 18);
  }
}
