package com.lothrazar.cyclic.item.craftingsave;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CraftingStickScreen extends ScreenBase<CraftingStickContainer> {

  public CraftingStickScreen(CraftingStickContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
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
    //    this.addButton(new ExtendedButton(guiLeft + 84, guiTop + 62, 8, 8, new StringTextComponent(ForgeI18n.parseMessage("")), b -> {
    //      //pressed
    //      for (int i = 1; i <= 9; i++) {
    //        this.container.transferStackInSlot(Minecraft.getInstance().player, i);
    //      }
    //      this.container.craftResult.clear();
    //    }));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.V_CRAFTING);
  }
}
