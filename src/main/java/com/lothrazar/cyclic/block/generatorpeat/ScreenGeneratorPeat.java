package com.lothrazar.cyclic.block.generatorpeat;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenGeneratorPeat extends ScreenBase<ContainerGeneratorPeat> {

  private ButtonMachine btnToggle;
  private ButtonMachineRedstone btnRedstone;
  private EnergyBar energy;
  private TimerBar timer;

  public ScreenGeneratorPeat(ContainerGeneratorPeat screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorPeat.MENERGY);
    timer = new TimerBar(this, 78, 55, TileMelter.TIMER_FULL);
  }

  @Override
  public void init() {
    super.init();
    timer.guiLeft = energy.guiLeft = guiLeft;
    timer.guiTop = energy.guiTop = guiTop;
    energy.guiTop = guiTop;
    int x = guiLeft + 132, y = guiTop + 8;
    btnToggle = addButton(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      container.tile.setFlowing((container.tile.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileGeneratorPeat.Fields.FLOWING.ordinal(), container.tile.getFlowing(), container.tile.getPos()));
    }));
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileGeneratorPeat.Fields.REDSTONE.ordinal(), container.tile.getPos()));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.tile.getFlowing()));
    btnToggle.setTextureId(container.tile.getFlowing() == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
    btnRedstone.onValueUpdate(container.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, xSize / 2 - 9, 28);
    energy.draw(ms, container.tile.getEnergy());
    timer.draw(ms, container.tile.getField(TileGeneratorPeat.Fields.BURNTIME.ordinal()));
  }
}
