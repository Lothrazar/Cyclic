package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenWorkbench extends ScreenBase<ContainerWorkbench> {
  public ScreenWorkbench(ContainerWorkbench screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {
    this.drawName(ms, title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);

    for (int colPos = 0; colPos < ContainerWorkbench.GRID_NUM_ROWS; colPos++) {
      for (int rowPos = 0; rowPos < ContainerWorkbench.GRID_NUM_ROWS; rowPos++) {
        this.drawSlot(ms,
              ContainerWorkbench.GRID_START_X - 1 + colPos * Const.SQ,
                ContainerWorkbench.GRID_START_Y - 1 + rowPos * Const.SQ);
      }
    }
    this.drawSlot(ms,
            ContainerWorkbench.OUTPUT_START_X - 1,
            ContainerWorkbench.OUTPUT_START_Y - 1);
  }
}
