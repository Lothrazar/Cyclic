package com.lothrazar.cyclicmagic.component.fan;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonFan extends GuiButtonTooltip {
  private final BlockPos tilePos;
  private TileEntityFan.Fields type;
  private int size;
  public ButtonFan(BlockPos current, int buttonId, int x, int y, int w, int h, int val, TileEntityFan.Fields strType) {
    super(buttonId, x, y, w, h, "");
    tilePos = current;
    type = strType;
    size = val;
    //  String ud = (size>0) ? "up" : "down";  
    setTooltip("button.fan." + type.name().toLowerCase() + ".tooltip");
  }
  public TileEntityFan.Fields getField() {
    return type;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileFan(tilePos, size, type));
    }
    return pressed;
  }
  public void updateDisplayStringWith(TileEntityFan tile) {
    this.displayString = UtilChat.lang("button.fan." + type.name().toLowerCase() + tile.getField(type.ordinal()));
  }
}
