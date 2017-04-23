package com.lothrazar.cyclicmagic.component.controlledminer;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTileMineHeight;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonMinerHeight extends GuiButton implements ITooltipButton {
  private final BlockPos tilePos;
  private final List<String> tooltips = new ArrayList<String>();
  boolean goUp;
  private String stype;//TODO: should be field ordinal/int not string but meh
  public ButtonMinerHeight(BlockPos current, int buttonId, int x, int y, boolean up, TileEntityControlledMiner.Fields list) {
    super(buttonId, x, y, 15, 10, "");
    tilePos = current;
    goUp = up;
    stype = list.name().toLowerCase();
    tooltips.add(TextFormatting.GRAY + UtilChat.lang("button." + stype + "." + (goUp ? "up" : "down")));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      int size = (goUp) ? 1 : -1;
      ModCyclic.network.sendToServer(new PacketTileMineHeight(tilePos, size, stype));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
}
