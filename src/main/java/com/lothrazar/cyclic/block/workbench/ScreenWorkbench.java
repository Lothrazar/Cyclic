package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.CraftingActionEnum;
import com.lothrazar.cyclic.gui.ButtonTextured;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketCraftAction;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenWorkbench extends ScreenBase<ContainerWorkbench> {

  public ScreenWorkbench(ContainerWorkbench screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
    int x = guiLeft + 108;
    int y = guiTop + 62;
    int size = 14;
    this.addButton(new ButtonTextured(x, y, size, size, TextureEnum.CRAFT_EMPTY, "cyclic.gui.craft.empty", b -> {
      //pressed
      PacketRegistry.INSTANCE.sendToServer(new PacketCraftAction(CraftingActionEnum.EMPTY));
    }));
    //
    x += 18;
    this.addButton(new ButtonTextured(x, y, size, size, TextureEnum.CRAFT_BALANCE, "cyclic.gui.craft.balance", b -> {
      PacketRegistry.INSTANCE.sendToServer(new PacketCraftAction(CraftingActionEnum.SPREAD));
    }));
    x += 18;
    this.addButton(new ButtonTextured(x, y, size, size, TextureEnum.CRAFT_MATCH, "cyclic.gui.craft.match", b -> {
      PacketRegistry.INSTANCE.sendToServer(new PacketCraftAction(CraftingActionEnum.SPREADMATCH));
    }));
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    //    super.drawGuiContainerForegroundLayer(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    this.drawButtonTooltips(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    //previous was fine, but this references exactly the 'minecraft:' vanilla crafting table
    this.drawBackground(ms, TextureRegistry.V_CRAFTING);
  }
}
