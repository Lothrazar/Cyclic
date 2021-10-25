package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenGeneratorFuel extends ScreenBase<ContainerGeneratorFuel> {

  private ButtonMachineField btnRedstone;
  private ButtonMachine btnToggle;
  private EnergyBar energy;
  private TexturedProgress progress;

  public ScreenGeneratorFuel(ContainerGeneratorFuel screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorFuel.MAX);
    this.progress = new TexturedProgress(this, 76, 60, TextureRegistry.FUEL_PROG);
  }

  @Override
  public void init() {
    super.init();
    energy.visible = true; //TileGeneratorFuel.POWERCONF.get() > 0;
    progress.guiLeft = energy.guiLeft = guiLeft;
    progress.guiTop = energy.guiTop = guiTop;
    int x, y;
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileGeneratorFuel.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    x = guiLeft + 132;
    y = guiTop + 36;
    btnToggle = addButton(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      int f = TileGeneratorFuel.Fields.FLOWING.ordinal();
      int tog = (container.tile.getField(f) + 1) % 2;
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, tog, container.tile.getPos()));
    }));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
    progress.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getField(TileGeneratorFuel.Fields.TIMER.ordinal()));
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int fld = TileGeneratorFuel.Fields.FLOWING.ordinal();
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.tile.getField(fld)));
    btnToggle.setTextureId(container.tile.getField(fld) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.drawSlot(ms, 54, 34); 
    this.drawSlotLarge(ms, 70, 30);
    energy.draw(ms, container.tile.getEnergy());
    progress.max = container.tile.getField(TileGeneratorFuel.Fields.BURNMAX.ordinal());
    progress.draw(ms, container.tile.getField(TileGeneratorFuel.Fields.TIMER.ordinal()));
  }
}
