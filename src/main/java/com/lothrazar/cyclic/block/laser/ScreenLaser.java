package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ScreenLaser extends ScreenBase<ContainerLaser> {

  private ButtonMachineField btnRedstone;
  private ButtonMachine btnX;
  private ButtonMachine btnY;
  private ButtonMachine btnZ;

  public ScreenLaser(ContainerLaser screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn, 176, 256);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileLaser.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += 26;
    int w = 160;
    int h = 14;
    int f = TileLaser.Fields.RED.ordinal();
    GuiSliderInteger red = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 255, menu.tile.getField(f)));
    red.setTooltip(Tooltip.create(Component.translatable("cyclic.screen.red")));
    y += h + 1;
    f = TileLaser.Fields.GREEN.ordinal();
    GuiSliderInteger green = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 255, menu.tile.getField(f)));
    green.setTooltip(Tooltip.create(Component.translatable("cyclic.screen.green")));
    y += h + 1;
    f = TileLaser.Fields.BLUE.ordinal();
    GuiSliderInteger blue = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 255, menu.tile.getField(f)));
    blue.setTooltip(Tooltip.create(Component.translatable("cyclic.screen.blue")));
    y += h + 1;
    f = TileLaser.Fields.ALPHA.ordinal();
    GuiSliderInteger alpha = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 100, menu.tile.getField(f)));
    alpha.setTooltip(Tooltip.create(Component.translatable("cyclic.screen.alpha")));
    y += h + 1;
    f = TileLaser.Fields.THICK.ordinal();
    GuiSliderInteger thick = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 20, menu.tile.getField(f)));
    thick.setTooltip(Tooltip.create(Component.translatable("cyclic.screen.thick")));
    //
    //
    //
    //
    w = 50;
    y += h + 2;
    btnX = addRenderableWidget(new ButtonMachine(x, y, w, 20, "X", (p) -> {
      final int fl = TileLaser.Fields.XOFF.ordinal();
      menu.tile.setField(fl, menu.tile.getField(fl) + 1);
      ClientPacketDistributor.sendToServer(new PacketTileData(fl, menu.tile.getField(fl), menu.tile.getBlockPos()));
    }));
    btnX.setTooltip(Tooltip.create(Component.translatable("button.offsetx.tooltip")));
    //
    x += w + 2;
    btnY = addRenderableWidget(new ButtonMachine(x, y, w, 20, "Y", (p) -> {
      final int fl = TileLaser.Fields.YOFF.ordinal();
      menu.tile.setField(fl, menu.tile.getField(fl) + 1);
      ClientPacketDistributor.sendToServer(new PacketTileData(fl, menu.tile.getField(fl), menu.tile.getBlockPos()));
    }));
    btnY.setTooltip(Tooltip.create(Component.translatable("button.offsety.tooltip")));
    //
    x += w + 2;
    btnZ = addRenderableWidget(new ButtonMachine(x, y, w, 20, "z", (p) -> {
      final int fl = TileLaser.Fields.ZOFF.ordinal();
      menu.tile.setField(fl, menu.tile.getField(fl) + 1);
      ClientPacketDistributor.sendToServer(new PacketTileData(fl, menu.tile.getField(fl), menu.tile.getBlockPos()));
    }));
    btnZ.setTooltip(Tooltip.create(Component.translatable("button.offsetz.tooltip")));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    this.drawSlot(ms, 151, 7, TextureRegistry.SLOT_GPS, 18);
    btnX.setMessage(ChatUtil.ilang("button.offsetblock.name" +
        menu.tile.getField(TileLaser.Fields.XOFF.ordinal())));
    btnY.setMessage(ChatUtil.ilang("button.offsetblock.name" +
        menu.tile.getField(TileLaser.Fields.YOFF.ordinal())));
    btnZ.setMessage(ChatUtil.ilang("button.offsetblock.name" +
        menu.tile.getField(TileLaser.Fields.ZOFF.ordinal())));
  }
}
