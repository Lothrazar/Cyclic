package com.lothrazar.cyclicmagic.component.pattern;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonPattern extends GuiButtonTooltip {
  private final BlockPos tilePos;
  private TileEntityPatternBuilder.Fields type;
  boolean goUp;
  public ButtonPattern(BlockPos current, int buttonId, int x, int y, boolean up, TileEntityPatternBuilder.Fields t) {
    super(buttonId, x, y, 15, 10, "");
    tilePos = current;
    type = t;
    goUp = up;
    String ud = (up) ? "up" : "down";
    setTooltip("tile.builder_pattern." + t.name().toLowerCase() + ud);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTilePatternBuilder(tilePos, goUp, type));
    }
    return pressed;
  }
}
