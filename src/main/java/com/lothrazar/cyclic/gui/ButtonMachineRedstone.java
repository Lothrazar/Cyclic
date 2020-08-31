package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.util.math.BlockPos;

public class ButtonMachineRedstone extends ButtonMachine {

  public ButtonMachineRedstone(int xPos, int yPos, int field, BlockPos pos) {
    super(xPos, yPos, 20, 20, "", (p) -> {
      //
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(field, pos));
    });
    this.tilePos = pos;
    this.setTileField(field);
  }

  public void onValueUpdate(TileEntityBase tile) {
    int val = tile.getField(this.getTileField());
    this.onValueUpdate(val);
  }

  private void onValueUpdate(int val) {
    setTooltip(UtilChat.lang("gui.cyclic.redstone" + val));
    setTextureId(val == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
  }
}
