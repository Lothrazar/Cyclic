package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CraftingBagScreen extends ScreenBase<CraftingBagContainer> {

  public CraftingBagScreen(CraftingBagContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
    //it works but its fkn broken, it only hits clientside lol
    //TODO for later i guess
    //    this.addButton(new ExtendedButton(guiLeft + 84, guiTop + 62, 8, 8, new StringTextComponent(ForgeI18n.parseMessage("")), b -> {
    //      //pressed
    //      this.container.craftResult.clear();
    //      for (int i = 1; i <= 9; i++) {
    //        ModCyclic.LOGGER.info("transfer attempt ");
    //        this.container.transferStackInSlot(Minecraft.getInstance().player, i);
    //      }
    //    }));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.V_CRAFTING);
  }
}
