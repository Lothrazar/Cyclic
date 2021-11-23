/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.block.crafter;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCrafter extends ScreenBase<ContainerCrafter> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private TexturedProgress progress;

  public ScreenCrafter(ContainerCrafter screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageHeight = 256;
    this.energy = new EnergyBar(this, TileCrafter.MAX);
    this.energy.setHeight(120);
    this.progress = new TexturedProgress(this, ContainerCrafter.PREVIEW_START_X - 3, ContainerCrafter.PREVIEW_START_Y + Const.SQ, 24, 17, TextureRegistry.ARROW);
    this.progress.max = TileCrafter.TIMER_FULL;
    this.progress.setTopDown(false);
  }

  @Override
  protected void init() {
    super.init();
    int x, y;
    energy.guiLeft = progress.guiLeft = leftPos;
    energy.guiTop = progress.guiTop = topPos;
    energy.visible = TileCrafter.POWERCONF.get() > 0;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileCrafter.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    super.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    energy.draw(ms, menu.tile.getEnergy());
    for (int rowPos = 0; rowPos < TileCrafter.IO_NUM_ROWS; rowPos++) {
      for (int colPos = 0; colPos < TileCrafter.IO_NUM_COLS; colPos++) {
        this.drawSlot(ms, ContainerCrafter.INPUT_START_X - 1 + colPos * Const.SQ,
            ContainerCrafter.INPUT_START_Y - 1 + rowPos * Const.SQ);
        this.drawSlot(ms, ContainerCrafter.OUTPUT_START_X - 1 + colPos * Const.SQ,
            ContainerCrafter.OUTPUT_START_Y - 1 + rowPos * Const.SQ);
      }
    }
    for (int colPos = 0; colPos < TileCrafter.GRID_NUM_ROWS; colPos++) {
      for (int rowPos = 0; rowPos < TileCrafter.GRID_NUM_ROWS; rowPos++) {
        this.drawSlot(ms,
            ContainerCrafter.GRID_START_X - 1 + colPos * Const.SQ,
            ContainerCrafter.GRID_START_Y - 1 + rowPos * Const.SQ);
      }
    }
    progress.draw(ms, menu.tile.getField(TileCrafter.Fields.TIMER.ordinal()));
  }
}
