package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenSoundRecorder extends ScreenBase<ContainerSoundRecorder> {

  public ScreenSoundRecorder(ContainerSoundRecorder screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageHeight = this.imageWidth = 256;
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 4;
    y = topPos + 174;
    int bsize = 20;
    final String pf = "block.cyclic.sound_recorder.";
    ButtonMachine buttonClear = addRenderableWidget(new ButtonMachine(x, y, bsize, bsize, TextureEnum.CRAFT_EMPTY, TileSoundRecorder.Fields.CLEARALL.ordinal(), (p) -> {
      menu.tile.clearSounds();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.CLEARALL.ordinal(), 1, menu.tile.getBlockPos()));
    }));
    buttonClear.xOffset = -3;
    buttonClear.yOffset = -2;
    buttonClear.setTooltip(pf + "clear");
    bsize = 16;
    y = topPos + 8;
    for (int i = 0; i < TileSoundRecorder.MAX_SOUNDS; i++) {
      ButtonMachine btnSave = addRenderableWidget(new ButtonMachine(x, y, bsize, bsize,
          TextureEnum.RENDER_SHOW, i, (p) -> {
            int soundIndex = ((ButtonMachine) p).getTileField();
            PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.SAVE.ordinal(), soundIndex, menu.tile.getBlockPos()));
          }));
      //      btnSave.active = !container.tile.inputSlots.getStackInSlot(0).isEmpty();
      btnSave.xOffset = 2;
      btnSave.yOffset = 2;
      btnSave.setTooltip(pf + "save");
      //      btnSave.setTextureId(TextureEnum.RENDER_SHOW);
      ButtonMachine btnIgnore = addRenderableWidget(new ButtonMachine(x + bsize, y, bsize, bsize,
          TextureEnum.POWER_STOP, i, (p) -> {
            int soundIndex = ((ButtonMachine) p).getTileField();
            menu.tile.ignoreSound(soundIndex);
            PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileSoundRecorder.Fields.IGNORE.ordinal(), soundIndex, menu.tile.getBlockPos()));
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
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    //    this.drawName(ms, this.title.getString());
    int x = 38, y = 12;
    for (int i = 0; i < TileSoundRecorder.MAX_SOUNDS; i++) {
      String s = menu.tile.getFieldString(i);
      this.drawString(ms, s, x, y);
      y += 16;
    }
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_SOUND);
    this.drawSlot(ms, 8, 208, TextureRegistry.SLOT_SOUND);
  }
}
