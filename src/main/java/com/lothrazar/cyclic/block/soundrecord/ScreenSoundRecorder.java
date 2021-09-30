package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.TextureEnum;
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
    x = guiLeft + 4;
    y = guiTop + 174;
    int bsize = 20;
    final String pf = "block.cyclic.sound_recorder.";
    ButtonMachine buttonClear = addButton(new ButtonMachine(x, y, bsize, bsize, TextureEnum.CRAFT_EMPTY, TileSoundRecorder.Fields.CLEARALL.ordinal(), (p) -> {
      container.tile.clearSounds();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.CLEARALL.ordinal(), 1, container.tile.getPos()));
    }));
    buttonClear.xOffset = -3;
    buttonClear.yOffset = -2;
    buttonClear.setTooltip(pf + "clear");
    bsize = 16;
    y = guiTop + 8;
    for (int i = 0; i < TileSoundRecorder.MAX_SOUNDS; i++) {
      ButtonMachine btnSave = addButton(new ButtonMachine(x, y, bsize, bsize,
          TextureEnum.RENDER_SHOW, i, (p) -> {
            int soundIndex = ((ButtonMachine) p).getTileField();
            PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.SAVE.ordinal(), soundIndex, container.tile.getPos()));
          }));
      //      btnSave.active = !container.tile.inputSlots.getStackInSlot(0).isEmpty();
      btnSave.xOffset = 2;
      btnSave.yOffset = 2;
      btnSave.setTooltip(pf + "save");
      //      btnSave.setTextureId(TextureEnum.RENDER_SHOW);
      ButtonMachine btnIgnore = addButton(new ButtonMachine(x + bsize, y, bsize, bsize,
          TextureEnum.POWER_STOP, i, (p) -> {
            int soundIndex = ((ButtonMachine) p).getTileField();
            container.tile.ignoreSound(soundIndex);
            PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.IGNORE.ordinal(), soundIndex, container.tile.getPos()));
          }));
      btnIgnore.xOffset = -1;
      btnIgnore.yOffset = -1;
      btnIgnore.setTooltip(pf + "ignore");
      //      btnIgnore.setTextureId();
      y += bsize;
    }
    //    buttonClear.setTooltip(pf + "clear");
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
    int x = 38, y = 12;
    for (int i = 0; i < TileSoundRecorder.MAX_SOUNDS; i++) {
      String s = container.tile.getFieldString(i);
      this.drawString(ms, s, x, y);
      y += 16;
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_SOUND);
    this.drawSlot(ms, 8, 208);
  }
}
