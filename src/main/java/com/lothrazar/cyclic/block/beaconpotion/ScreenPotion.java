package com.lothrazar.cyclic.block.beaconpotion;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenPotion extends ScreenBase<ContainerPotion> {

  private ButtonMachine btnEntity;
  private ButtonMachineRedstone btnRedstone;
  private EnergyBar energy;

  public ScreenPotion(ContainerPotion screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    energy = new EnergyBar(this, TilePotion.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TilePotion.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    y += 51;
    //TODO  refactor btn
    btnEntity = addButton(new ButtonMachine(x, y, 60, 20, "", (p) -> {
      int f = TilePotion.Fields.ENTITYTYPE.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          container.tile.getField(f) + 1, container.tile.getPos()));
    }));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
    btnEntity.setTooltip(UtilChat.lang("cyclic.beacon.entitytype.tooltip"));
    btnEntity.setMessage(UtilChat.ilang("cyclic.entitytype." + container.tile.entityFilter.name().toLowerCase()));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
    this.drawSlot(ms, 8, 34);
    int x = guiLeft + 29, y = guiTop + 16;
    this.drawString(ms, container.tile.getTimerDisplay(), x, y);
    for (String s : container.tile.getPotionDisplay()) {
      y += 10;
      this.drawString(ms, s, x, y);
    }
  }
}
