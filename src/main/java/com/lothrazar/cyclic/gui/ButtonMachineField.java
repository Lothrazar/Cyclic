package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.core.BlockPos;

public class ButtonMachineField extends ButtonMachine {

  BlockPos tilePos;
  private TextureEnum textureZero;
  private TextureEnum textureOne;
  private TextureEnum textureTwo = TextureEnum.RENDER_OUTLINE;
  private String tooltipPrefix;

  public ButtonMachineField(int xPos, int yPos, int field, BlockPos pos) {
    this(xPos, yPos, field, pos, TextureEnum.REDSTONE_ON, TextureEnum.REDSTONE_NEEDED, "gui.cyclic.redstone");
  }

  public ButtonMachineField(int xPos, int yPos, int field, BlockPos pos,
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

  public ButtonMachineField setSize(int size) {
    this.height = size;
    this.width = size;
    return this;
  }

  public void onValueUpdate(TileBlockEntityCyclic tile) {
    int val = tile.getField(this.getTileField());
    this.onValueUpdate(val);
  }

  private void onValueUpdate(int val) {
    setTooltip(ChatUtil.lang(this.tooltipPrefix + val));
    // PreviewOutlineType.NONE.ordinal(); // TODO: use enum in switch
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
