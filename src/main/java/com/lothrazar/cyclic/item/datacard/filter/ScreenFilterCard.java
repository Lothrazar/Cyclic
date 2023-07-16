package com.lothrazar.cyclic.item.datacard.filter;

import com.lothrazar.cyclic.data.CraftingActionEnum;
import com.lothrazar.cyclic.gui.ButtonTextured;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFilterCard extends ScreenBase<ContainerFilterCard> {

  private ButtonTextured btnType;
  private final ContainerFilterCard screenContainer;

  public ScreenFilterCard(ContainerFilterCard screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.screenContainer = screenContainer;
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  public void init() {
    super.init();
    int x = leftPos + 150;
    int y = topPos + 8;
    final int size = 20;
    btnType = this.addRenderableWidget(new ButtonTextured(x, y, size, size, TextureEnum.RENDER_HIDE, "", b -> {
      //pressed
      PacketRegistry.INSTANCE.sendToServer(new PacketFilterCard(CraftingActionEnum.EMPTY));
      FilterCardItem.toggleFilterType(screenContainer.bag);
    }));
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    super.renderLabels(ms, mouseX, mouseY);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    boolean filter = screenContainer.bag.getOrCreateTag().getBoolean("filter");
    btnType.setTextureId(filter ? TextureEnum.RENDER_HIDE : TextureEnum.RENDER_SHOW);
    btnType.setTooltip("cyclic.screen.filter." + filter);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int i = 0; i < 9; i++) {
      int y = 31;
      if (i == FilterCardItem.SLOT_FLUID) {
        this.drawSlot(ms, 7 + i * Const.SQ, y, TextureRegistry.SLOT_BUCKET, 18);
      }
      else {
        this.drawSlot(ms, 7 + i * Const.SQ, y);
      }
    }
  }
}
