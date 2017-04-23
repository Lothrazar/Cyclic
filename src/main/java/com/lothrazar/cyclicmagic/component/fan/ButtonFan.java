package com.lothrazar.cyclicmagic.component.fan;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTileFan;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonFan extends GuiButton implements ITooltipButton {
  private final BlockPos tilePos;
  private final List<String> tooltips = new ArrayList<String>();
  private TileEntityFan.Fields type;
  private int size;
  public ButtonFan(BlockPos current, int buttonId, int x, int y, int w, int h, int val, TileEntityFan.Fields strType) {
    super(buttonId, x, y, w, h, "");
    tilePos = current;
    type = strType;
    size = val;
    //  String ud = (size>0) ? "up" : "down";  
    tooltips.add(TextFormatting.GRAY + UtilChat.lang("button.fan." + type.name().toLowerCase() + ".tooltip"));
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
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
  public void updateDisplayStringWith(TileEntityFan tile) {
    this.displayString = UtilChat.lang("button.fan." + type.name().toLowerCase() + tile.getField(type.ordinal()));
  }
}
