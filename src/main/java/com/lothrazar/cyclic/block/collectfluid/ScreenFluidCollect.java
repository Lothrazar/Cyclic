package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFluidCollect extends ScreenBase<ContainerFluidCollect> {

  private FluidBar fluid;
  private ButtonMachineRedstone btnRedstone;
  private ButtonMachineRedstone btnRender;

  public ScreenFluidCollect(ContainerFluidCollect screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 152, 14, TileFluidCollect.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = guiLeft;
    fluid.guiTop = guiTop;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileFluidCollect.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    btnRender = addButton(new ButtonMachineRedstone(x + 20, y, TileFluidCollect.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //    btnRender = addButton(new ButtonMachine(x + 20, y, 20, 20, "", (p) -> {
    //      int f = TileFluidCollect.Fields.RENDER.ordinal();
    //      container.tile.setField(f, (container.tile.getField(f) + 1) % 2);
    //      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    //    }));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getFluid());
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
    this.drawSlot(ms, xSize / 2 - 9, 28);
    fluid.draw(ms, container.tile.getFluid());
  }
}
