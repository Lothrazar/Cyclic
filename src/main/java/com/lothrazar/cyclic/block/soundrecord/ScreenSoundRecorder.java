package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenSoundRecorder extends ScreenBase<ContainerSoundRecorder> {

  public ScreenSoundRecorder(ContainerSoundRecorder screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.ySize = this.xSize = 256;
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 150;
    y = guiTop + 4;
    ButtonMachine buttonClear = addButton(new ButtonMachine(x, y, 20, 20, "", (p) -> {
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.CLEARALL.ordinal(), 1, container.tile.getPos()));
    }));
    x = guiLeft + 16;
    y = guiTop;
    int bsize = 16;
    final String pf = "block.cyclic.sound_recorder.";
    for (int i = 0; i < TileSoundRecorder.MAX_SOUNDS; i++) {
      //      TextFieldWidget w = new TextFieldWidget(this.font, x, y - 20, width, ht, new StringTextComponent(""));
      ButtonMachine btnSave = addButton(new ButtonMachine(x, y, bsize, bsize,
          "SAVE", (p) -> {
            int soundIndex = ((ButtonMachine) p).getTileField();
            PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.SAVE.ordinal(), soundIndex, container.tile.getPos()));
          }));
      btnSave.setTooltip(pf + ".save");
      x += bsize;
      ButtonMachine btnIgnore = addButton(new ButtonMachine(x, y, bsize, bsize,
          "IGNORE", (p) -> {
            int soundIndex = ((ButtonMachine) p).getTileField();
            PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.IGNORE.ordinal(), soundIndex, container.tile.getPos()));
          }));
      btnIgnore.setTooltip(pf + ".ignore");
      y += bsize;
    }
    buttonClear.setTooltip(pf + ".clear");
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    //    this.drawName(ms, this.title.getString());
    int x = 28, y = 0;
    for (int i = 0; i < TileSoundRecorder.MAX_SOUNDS; i++) {
      String s = container.tile.getFieldString(i);
      this.drawString(ms, s, x, y);
      y += 10;
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_SOUND);
    this.drawSlot(ms, 154, 34);
  }
}
