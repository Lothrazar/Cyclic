package com.lothrazar.cyclic.block.generatorpeat;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenGeneratorPeat extends ScreenBase<ContainerGeneratorPeat> {

  private ButtonMachine btnToggle;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private TexturedProgress progress;

  public ScreenGeneratorPeat(ContainerGeneratorPeat screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorPeat.MENERGY);
    this.progress = new TexturedProgress(this, 76, 60, TextureRegistry.FUEL_PROG);
    progress.max = TileGeneratorPeat.BURNTIME;
  }

  @Override
  public void init() {
    super.init();
    progress.guiLeft = energy.guiLeft = guiLeft;
    progress.guiTop = energy.guiTop = guiTop;
    energy.guiTop = guiTop;
    int x = guiLeft + 132, y = guiTop + 36;
    btnToggle = addButton(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      container.tile.setFlowing((container.tile.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileGeneratorPeat.Fields.FLOWING.ordinal(), container.tile.getFlowing(), container.tile.getPos()));
    }));
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileGeneratorPeat.Fields.REDSTONE.ordinal(), container.tile.getPos()));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
    progress.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getField(TileGeneratorPeat.Fields.BURNTIME.ordinal()));
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
    this.drawSlotLarge(ms, 70, 30);
    energy.draw(ms, container.tile.getEnergy());
    progress.draw(ms, container.tile.getField(TileGeneratorPeat.Fields.BURNTIME.ordinal()));
  }
}
