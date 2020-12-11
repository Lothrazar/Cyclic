package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageBagScreen extends ScreenBase<StorageBagContainer> {
  public StorageBagScreen(StorageBagContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {
    drawString(ms, this.container.bag.getTranslationKey(),
            (this.getXSize() - this.font.getStringWidth(this.container.bag.getTranslationKey())) / 2,
            6.0F);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.container.bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        int row = (int) i / 9;
        int col = i % 9;
        int xPos = 7 + col * Const.SQ;
        int yPos = 18 + row * Const.SQ;

        this.drawSlot(ms, xPos, yPos);
      }
    });
  }
}
