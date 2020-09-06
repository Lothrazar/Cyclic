package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.util.math.BlockPos;

public class ButtonMachineRedstone extends ButtonMachine {

  private TextureEnum textureOne;
  private TextureEnum textureZero;
  private String tooltipPrefix;

  public ButtonMachineRedstone(int xPos, int yPos, int field, BlockPos pos) {
    this(xPos, yPos, field, pos, TextureEnum.REDSTONE_ON, TextureEnum.REDSTONE_NEEDED, "gui.cyclic.redstone");
  }

  public ButtonMachineRedstone(int xPos, int yPos, int field, BlockPos pos,
      TextureEnum toff, TextureEnum tonn, String tooltipPrefix) {
    super(xPos, yPos, 20, 20, "", (p) -> {
      //save included 
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(field, pos));
    });
    this.tilePos = pos;
    this.setTileField(field);
    this.textureZero = toff;
    this.textureOne = tonn;
    this.tooltipPrefix = tooltipPrefix;
  }

  public void onValueUpdate(TileEntityBase tile) {
    int val = tile.getField(this.getTileField());
    this.onValueUpdate(val);
  }

  private void onValueUpdate(int val) {
    setTooltip(UtilChat.lang(this.tooltipPrefix + val));
    setTextureId(val == 1 ? textureOne : textureZero);
  }
}
