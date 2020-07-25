package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenHarvester extends ScreenBase<ContainerHarvester> {

  private EnergyBar energy;
  private ButtonMachine btnRedstone;

  public ScreenHarvester(ContainerHarvester screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileHarvester.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachine(x, y, 20, 20, "", (p) -> {
      container.tile.setNeedsRedstone((container.getNeedsRedstone() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileHarvester.Fields.REDSTONE.ordinal(), container.tile.getNeedsRedstone(), container.tile.getPos()));
    }));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.func_230459_a_(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.setTooltip(UtilChat.lang("gui.cyclic.redstone" + container.getNeedsRedstone()));
    btnRedstone.setTextureId(container.getNeedsRedstone() == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.getEnergy());
  }
}
