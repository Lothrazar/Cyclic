package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFluidCollect extends ScreenBase<ContainerFluidCollect> {

  private EnergyBar energy;
  private FluidBar fluid;
  private ButtonMachineRedstone btnRedstone;
  private ButtonMachineRedstone btnRender;

  public ScreenFluidCollect(ContainerFluidCollect screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, TileFluidCollect.CAPACITY);
    energy = new EnergyBar(this, TileFluidCollect.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = fluid.guiLeft = guiLeft;
    energy.guiTop = fluid.guiTop = guiTop;
    energy.visible = TileFluidCollect.POWERCONF.get() > 0;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileFluidCollect.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    btnRender = addButton(new ButtonMachineRedstone(x, y + 20, TileFluidCollect.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getFluid());
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    //    int on = container.tile.getField(TileFluidCollect.Fields.RENDER.ordinal());
    //    btnRender.setTooltip(UtilChat.lang("gui.cyclic.render" + on));
    //    btnRender.setTextureId(on == 1 ? TextureEnum.RENDER_SHOW : TextureEnum.RENDER_HIDE);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
    fluid.draw(ms, container.tile.getFluid());
    this.drawSlot(ms, xSize / 2 - 9, 28);
  }
}
