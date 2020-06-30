package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenDisenchant extends ScreenBase<ContainerDisenchant> {

  private ButtonMachine btnRedstone;

  public ScreenDisenchant(ContainerDisenchant screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachine(x, y, 20, 20, "", (p) -> {
      container.tile.setNeedsRedstone((container.tile.getNeedsRedstone() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileDisenchant.Fields.REDSTONE.ordinal(), container.tile.getNeedsRedstone(), container.tile.getPos()));
    }));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawButtonTooltips(mouseX, mouseY);
    this.drawName(this.title.getFormattedText());
    btnRedstone.setTooltip(UtilChat.lang("gui.cyclic.redstone" + container.tile.getNeedsRedstone()));
    btnRedstone.setTextureId(container.tile.getNeedsRedstone() == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(xSize / 2 - 9, 28);
  }
}
