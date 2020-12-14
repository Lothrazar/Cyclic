package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

public class CraftingBagScreen extends ScreenBase<CraftingBagContainer> {

  public CraftingBagScreen(CraftingBagContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    //    this.ySize = 256;
  }

  @Override
  protected void init() {
    super.init();
    CompoundNBT nbt = this.container.bag.getOrCreateTag();
    net.minecraft.inventory.container.WorkbenchContainer xyz;
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {}

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.minecraft.getTextureManager().bindTexture(TextureRegistry.INVENTORY_SIDEBAR);
    //    Screen.blit(ms, this.guiLeft - 24, this.guiTop, 0, 0, 27, 101, 27, 101);
    //this.container.bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
    //  for (int i = 0; i < h.getSlots(); i++) {
    //    int row = (int) i / 9;
    //    int col = i % 9;
    //    int xPos = 7 + col * Const.SQ;
    //    int yPos = 18 + row * Const.SQ;
    //
    //    this.drawSlot(ms, xPos, yPos);
    //  }
    //});
  }
}
