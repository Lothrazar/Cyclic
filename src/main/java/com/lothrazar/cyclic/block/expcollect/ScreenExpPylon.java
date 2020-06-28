package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenExpPylon extends ScreenBase<ContainerExpPylon> {

  private FluidBar fluid;

  public ScreenExpPylon(ContainerExpPylon screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 8, 8, TileExpPylon.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = guiLeft;
    fluid.guiTop = guiTop;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    fluid.renderHoveredToolTip(mouseX, mouseY, container.tile.tank.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawButtonTooltips(mouseX, mouseY);
    this.drawName(this.title.getFormattedText());
    int xp = container.tile.getStoredXp();
    if (xp > 0)
      this.font.drawString(xp + " XP",
          (this.getXSize()) / 2 + 4,
          40.0F, 4209792);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    fluid.draw(container.tile.tank.getFluid());
  }
}
