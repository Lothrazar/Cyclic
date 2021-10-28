package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenExpPylon extends ScreenBase<ContainerExpPylon> {

  private ButtonMachineField btnRedstone;
  private FluidBar fluid;

  public ScreenExpPylon(ContainerExpPylon screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 150, 8, TileExpPylon.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = leftPos;
    fluid.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileExpPylon.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.tank.getFluid());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int xp = menu.tile.getStoredXp();
    if (xp >= 0) {
      this.font.draw(ms, xp + " XP", // TODO: lang tag
          (this.getXSize()) / 2 + 4, 40.0F, 4209792);
    }
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    fluid.draw(ms, menu.tile.tank.getFluid());
  }
}
