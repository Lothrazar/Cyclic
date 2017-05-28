package com.lothrazar.cyclicmagic.component.builder;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ITooltipButton;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildSize extends GuiButton implements ITooltipButton {
  private final BlockPos tilePos;
  private final List<String> tooltips = new ArrayList<String>();
  boolean goUp;
  private String type;
  public ButtonBuildSize(BlockPos current, int buttonId, int x, int y, int width, boolean up, String strType) {
    super(buttonId, x, y, 15, 10, "");
    tilePos = current;
    goUp = up;
    type = strType;
    tooltips.add(TextFormatting.GRAY + UtilChat.lang("button." + type + "." + (goUp ? "up" : "down")));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      int size = (goUp) ? 1 : -1;
      ModCyclic.network.sendToServer(new PacketTileBuildSize(tilePos, size, type));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
}
