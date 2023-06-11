package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGeneratorFuel extends ScreenBase<ContainerGeneratorFuel> {

  private ButtonMachineField btnRedstone;
  private ButtonMachine btnToggle;
  private EnergyBar energy;
  private TexturedProgress progress;

  public ScreenGeneratorFuel(ContainerGeneratorFuel screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this.font, TileGeneratorFuel.MAX);
    this.progress = new TexturedProgress(this.font    , 76, 60, TextureRegistry.FUEL_PROG);
  }

  @Override
  public void init() {
    super.init();
    energy.visible = true;
    progress.guiLeft = energy.guiLeft = leftPos;
    progress.guiTop = energy.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileGeneratorFuel.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    x = leftPos + 132;
    y = topPos + 36;
    btnToggle = addRenderableWidget(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      int f = TileGeneratorFuel.Fields.FLOWING.ordinal();
      int tog = (menu.tile.getField(f) + 1) % 2;
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, tog, menu.tile.getBlockPos()));
    }));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    progress.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getField(TileGeneratorFuel.Fields.TIMER.ordinal()));
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int fld = TileGeneratorFuel.Fields.FLOWING.ordinal();
    btnToggle.setTooltip(ChatUtil.lang("gui.cyclic.flowing" + menu.tile.getField(fld)));
    btnToggle.setTextureId(menu.tile.getField(fld) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlotLarge(ms, 70, 30);
    progress.max = menu.tile.getField(TileGeneratorFuel.Fields.BURNMAX.ordinal());
    progress.draw(ms, menu.tile.getField(TileGeneratorFuel.Fields.TIMER.ordinal()));
    energy.draw(ms, menu.tile.getEnergy());
  }
}
