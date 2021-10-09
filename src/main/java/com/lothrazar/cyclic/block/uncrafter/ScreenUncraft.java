package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenUncraft extends ScreenBase<ContainerUncraft> {

  private TimerBar timer;
  private EnergyBar energy;
  private ButtonMachineField btnRedstone;

  public ScreenUncraft(ContainerUncraft screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileUncraft.MAX);
    this.timer = new TimerBar(this, 58, 20, TileUncraft.TIMER.get());
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = timer.guiLeft = leftPos;
    energy.guiTop = timer.guiTop = topPos;
    energy.visible = TileUncraft.POWERCONF.get() > 0;
    timer.visible = TileUncraft.TIMER.get() > 1;
    int x, y;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileUncraft.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    if (menu.tile.getStatus() != UncraftStatusEnum.EMPTY) {
      String name = UtilChat.lang(
          ModCyclic.MODID + ".gui.uncrafter." + menu.tile.getStatus().name().toLowerCase());
      int center = (this.getXSize() - this.font.width(name)) / 2;
      drawString(ms, name, center + 37, 24);
    }
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
    this.drawSlot(ms, 38, 18);
    for (int i = 0; i < 8; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, 44);
      this.drawSlot(ms, 7 + i * Const.SQ, 44 + Const.SQ);
    }
    timer.draw(ms, menu.tile.getField(TileUncraft.Fields.TIMER.ordinal()));
  }
}
