package com.lothrazar.cyclic.item.lunchbox;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenLunchbox extends ScreenBase<ContainerLunchbox> {

  public ScreenLunchbox(ContainerLunchbox screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  protected void init() {
    super.init();
    //    CompoundTag nbt = this.menu.bag.getOrCreateTag();
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int colPos = 0; colPos < ItemLunchbox.SLOTS; colPos++) {
      this.drawSlot(ms, 25 + colPos * Const.SQ, 35);
    }
  }
}
