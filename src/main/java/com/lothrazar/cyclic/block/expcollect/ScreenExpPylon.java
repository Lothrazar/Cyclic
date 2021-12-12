package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenExpPylon extends ScreenBase<ContainerExpPylon> {

  private ButtonMachineField btnRedstone;
  private FluidBar fluid;

  public ScreenExpPylon(ContainerExpPylon screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 150, 8, TileExpPylon.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = guiLeft;
    fluid.guiTop = guiTop;
    int x, y;
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileExpPylon.Fields.REDSTONE.ordinal(), container.tile.getPos()));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.tank.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int xp = container.tile.getStoredXp();
    if (xp >= 0) {
      this.font.drawString(ms, xp + " XP", // TODO: lang tag
          (float) this.getXSize() / 2 + 4, 40.0F, 4209792);
    }
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    fluid.draw(ms, container.tile.tank.getFluid());
  }
}
