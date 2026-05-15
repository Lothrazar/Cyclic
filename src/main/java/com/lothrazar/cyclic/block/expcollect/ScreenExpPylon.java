package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.gui.FluidBar;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenExpPylon extends ScreenBase<ContainerExpPylon> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnCollect;
  private FluidBar fluid;

  public ScreenExpPylon(ContainerExpPylon screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    fluid = new FluidBar(this.font, 150, 8, TileExpPylon.CAPACITY);
    fluid.guiLeft = leftPos;
    fluid.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileExpPylon.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += Const.PAD * 3;
    btnCollect = addRenderableWidget(new ButtonMachineField(x, y, TileExpPylon.Fields.COLLECT.ordinal(), menu.tile.getBlockPos()));
    btnCollect.setWidth(70);
    btnCollect.setTooltip(Tooltip.create(Component.translatable("button.exp_pylon.collect.tooltip")));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms, mouseX, mouseY, partialTicks);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.tank.getFluid());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int xp = menu.tile.getStoredXp();
    if (xp >= 0) {
      drawString(ms, xp + " " + ChatUtil.lang("cyclic.screen.xp"), (float) this.getXSize() / 2 - 30, (int) 60.0F);
    }
    btnRedstone.onValueUpdate(menu.tile);
    btnCollect.setMessage(ChatUtil.ilang("button.exp_pylon.collect" + menu.tile.getField(TileExpPylon.Fields.COLLECT.ordinal())));
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    fluid.draw(ms, menu.tile.tank.getFluid());
  }
}
