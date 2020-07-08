package com.lothrazar.cyclic.block.structurewriter;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.structurewriter.TileWriter.StructureStatus;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenWriter extends ScreenBase<ContainerWriter> {

  private ButtonMachine btnMode;

  public ScreenWriter(ContainerWriter screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 16;
    btnMode = addButton(new ButtonMachine(x, y, 60, 20, "", (p) -> {
      int f = TileWriter.Fields.MODE.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          (container.tile.getField(f) + 1) % 2, container.tile.getPos()));
    }));
    btnMode.setTooltip(UtilChat.lang("cyclic.structure_writer.mode"));
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
    btnMode.setMessage(UtilChat.lang("cyclic.structure_writer.mode."
        + container.tile.mode.name().toLowerCase()));
    this.drawName(this.title.getFormattedText());
    StructureStatus status = this.container.tile.structStatus;
    this.drawString(UtilChat.lang("cyclic.structure_writer.status." + status.name().toLowerCase()),
        14, 40);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(77, 17, TextureRegistry.SLOT_DISK, 18);
    this.drawSlot(97, 17, TextureRegistry.SLOT_GPS, 18);
  }
}
