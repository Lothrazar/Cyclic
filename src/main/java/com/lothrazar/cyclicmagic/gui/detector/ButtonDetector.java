package com.lothrazar.cyclicmagic.gui.detector;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector.Fields;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTileDetector;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonDetector extends GuiButton implements ITooltipButton {
  private final BlockPos tilePos;
  private final List<String> tooltips = new ArrayList<String>();
  private Fields type;
  boolean goUp;
  public ButtonDetector(BlockPos current, int buttonId, int x, int y, boolean up, TileEntityDetector.Fields t, int w, int h) {
    super(buttonId, x, y, w, h, "");
    tilePos = current;
    type = t;
    goUp = up;
    String ud = "";
    if (type != Fields.ENTITYTYPE && type != Fields.GREATERTHAN) {
      ud = (up) ? "up" : "down";
    }
    tooltips.add(UtilChat.lang("tile.entity_detector." + t.name().toLowerCase() + ud));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileDetector(tilePos, goUp, type));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
}
