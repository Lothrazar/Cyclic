package com.lothrazar.cyclic.block.beaconpotion;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPotion extends ScreenBase<ContainerPotion> {

  private ButtonMachine btnEntity;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;

  public ScreenPotion(ContainerPotion screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    energy = new EnergyBar(this, TilePotion.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    energy.visible = TilePotion.POWERCONF.get() > 0;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TilePotion.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += 51;
    btnEntity = addRenderableWidget(new ButtonMachine(x, y, 60, 20, "", (p) -> {
      int f = TilePotion.Fields.ENTITYTYPE.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnEntity.setTooltip(UtilChat.lang("cyclic.beacon.entitytype.tooltip"));
    btnEntity.setMessage(UtilChat.ilang("cyclic.entitytype." + menu.tile.entityFilter.name().toLowerCase()));
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 148, 8, TextureRegistry.SLOT_FILTER, 18);
    energy.draw(ms, menu.tile.getEnergy());
    this.drawSlot(ms, 8, 34);
    int x = leftPos + 29, y = topPos + 16;
    this.drawString(ms, menu.tile.getTimerDisplay(), x, y);
    for (String s : menu.tile.getPotionDisplay()) {
      y += 10;
      this.drawString(ms, s, x, y);
    }
  }
}
