package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenUncraft extends ScreenBase<ContainerUncraft> {

  private TimerBar timer;
  private EnergyBar energy;
  private ButtonMachineField btnRedstone;

  public ScreenUncraft(ContainerUncraft screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileUncraft.MAX);
    this.timer = new TimerBar(this, 58, 20, TileUncraft.TIMER.get());
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = timer.guiLeft = guiLeft;
    energy.guiTop = timer.guiTop = guiTop;
    energy.visible = TileUncraft.POWERCONF.get() > 0;
    timer.visible = TileUncraft.TIMER.get() > 1;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileUncraft.Fields.REDSTONE.ordinal(), container.tile.getPos()));
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
    btnRedstone.onValueUpdate(container.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    if (container.tile.getStatus() != UncraftStatusEnum.EMPTY) {
      String name = UtilChat.lang(
          ModCyclic.MODID + ".gui.uncrafter." + container.tile.getStatus().name().toLowerCase());
      int center = (this.getXSize() - this.font.getStringWidth(name)) / 2;
      drawString(ms, name, center + 37, 24);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
    this.drawSlot(ms, 38, 18);
    for (int i = 0; i < 8; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, 44);
      this.drawSlot(ms, 7 + i * Const.SQ, 44 + Const.SQ);
    }
    timer.draw(ms, container.tile.getField(TileUncraft.Fields.TIMER.ordinal()));
  }
}
