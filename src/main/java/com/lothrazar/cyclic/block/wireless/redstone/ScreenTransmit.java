package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenTransmit extends ScreenBase<ContainerTransmit> {

  private ButtonMachineField btnRender;

  public ScreenTransmit(ContainerTransmit screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    super.init();
    int x, y;
    x = leftPos + 8;
    y = topPos + 16;
    btnRender = addButton(new ButtonMachineField(x, y, TileWirelessTransmit.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
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
    this.drawName(ms, title.getString());
    btnRender.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int s = 0; s < 9; s++) {
      this.drawSlot(ms, 8 + 18 * s, 48, TextureRegistry.SLOT_GPS, Const.SQ);
    }
  }
}
