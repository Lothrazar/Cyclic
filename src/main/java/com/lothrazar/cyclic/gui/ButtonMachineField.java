package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.PacketDistributor;

public class ButtonMachineField extends ButtonMachine {

  BlockPos tilePos;
  private TextureEnum textureZero;
  private TextureEnum textureOne;
  private TextureEnum textureTwo = TextureEnum.RENDER_OUTLINE;
  private String tooltipPrefix;

  public ButtonMachineField(int xPos, int yPos, int field, BlockPos pos) {
    this(xPos, yPos, field, pos, TextureEnum.REDSTONE_ON, TextureEnum.REDSTONE_NEEDED, "gui.cyclic.redstone");
  }

  public ButtonMachineField(int xPos, int yPos, int field, BlockPos pos, int w, int h) {
    this(xPos, yPos, field, pos);
    this.width = w;
    this.height = h;
  }

  public ButtonMachineField(int xPos, int yPos, int field, BlockPos pos,
      TextureEnum toff, TextureEnum tonn, String tooltipPrefix) {
    super(xPos, yPos, 20, 20, "", (p) -> {
      PacketDistributor.sendToServer(new PacketTileData(field, pos));
    });
    this.tilePos = pos;
    this.setTileField(field);
    this.textureZero = toff;
    this.textureOne = tonn;
    this.tooltipPrefix = tooltipPrefix;
  }

  public ButtonMachineField setSize(int size) {
    this.height = size;
    this.width = size;
    return this;
  }

  public void onValueUpdate(Object tile) {
    if (tile instanceof TileBlockEntityCyclic base) {
      this.onValueUpdate(base.getField(this.getTileField()));
    }
  }

  private void onValueUpdate(int val) {
    if(tooltipPrefix != null) {
      setTooltip(ChatUtil.lang(this.tooltipPrefix + val));
    }
    switch (val) {
      case 0:
        setTextureId(textureZero);
      break;
      case 1:
        setTextureId(textureOne);
      break;
      case 2:
        setTextureId(textureTwo);
      break;
    }
  }
}
