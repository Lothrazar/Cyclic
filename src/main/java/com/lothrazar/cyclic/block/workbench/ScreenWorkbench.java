package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.data.CraftingActionEnum;
import com.lothrazar.cyclic.gui.ButtonTextured;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketCraftAction;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenWorkbench extends ScreenBase<ContainerWorkbench> {

  public ScreenWorkbench(ContainerWorkbench screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x = leftPos + 108;
    int y = topPos + 62;
    int size = 14;
    this.addRenderableWidget(new ButtonTextured(x, y, size, size, TextureEnum.CRAFT_EMPTY, "cyclic.gui.craft.empty", b -> {
      //pressed
      PacketRegistry.INSTANCE.sendToServer(new PacketCraftAction(CraftingActionEnum.EMPTY));
    }));
    //
    x += 18;
    this.addRenderableWidget(new ButtonTextured(x, y, size, size, TextureEnum.CRAFT_BALANCE, "cyclic.gui.craft.balance", b -> {
      PacketRegistry.INSTANCE.sendToServer(new PacketCraftAction(CraftingActionEnum.SPREAD));
    }));
    x += 18;
    this.addRenderableWidget(new ButtonTextured(x, y, size, size, TextureEnum.CRAFT_MATCH, "cyclic.gui.craft.match", b -> {
      PacketRegistry.INSTANCE.sendToServer(new PacketCraftAction(CraftingActionEnum.SPREADMATCH));
    }));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }
  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    //    super.drawGuiContainerForegroundLayer(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    this.drawButtonTooltips(ms, mouseX, mouseY);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int x, int y) {
    //previous was fine, but this references exactly the 'minecraft:' vanilla crafting table
    this.drawBackground(ms, TextureRegistry.V_CRAFTING);
  }
}
