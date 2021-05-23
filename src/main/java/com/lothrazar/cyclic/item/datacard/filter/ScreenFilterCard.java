package com.lothrazar.cyclic.item.datacard.filter;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.data.CraftingActionEnum;
import com.lothrazar.cyclic.gui.ButtonTextured;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketFilterCard;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFilterCard extends ScreenBase<ContainerFilterCard> {

  private ButtonTextured btnType;
  private final ContainerFilterCard screenContainer;

  public ScreenFilterCard(ContainerFilterCard screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.screenContainer = screenContainer;
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  public void init() {
    super.init();
    int x = guiLeft + 150;
    int y = guiTop + 8;
    final int size = 20;
    btnType = this.addButton(new ButtonTextured(x, y, size, size, TextureEnum.RENDER_HIDE, "", b -> {
      //pressed
      PacketRegistry.INSTANCE.sendToServer(new PacketFilterCard(CraftingActionEnum.EMPTY));
      FilterCardItem.toggleFilterType(screenContainer.bag);
    }));
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(ms, mouseX, mouseY);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    boolean filter = screenContainer.bag.getOrCreateTag().getBoolean("filter");
    btnType.setTextureId(filter ? TextureEnum.RENDER_HIDE : TextureEnum.RENDER_SHOW);
    btnType.setTooltip("cyclic.screen.filter." + filter);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int i = 0; i < 9; i++) {
      int y = 31;
      this.drawSlot(ms, 7 + i * Const.SQ, y);
      //      this.drawSlot(ms, 7 + i * Const.SQ, y + Const.SQ);
    }
  }
}
