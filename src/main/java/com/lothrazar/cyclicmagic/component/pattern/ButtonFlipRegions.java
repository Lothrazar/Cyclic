package com.lothrazar.cyclicmagic.component.pattern;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.pattern.PacketTilePatternSwap.SwapType;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonFlipRegions extends GuiButtonTooltip {
  private final BlockPos tilePos;
  public ButtonFlipRegions(int buttonId, int x, int y, BlockPos current) {
    super(buttonId, x, y, 24, 12, "");
    tilePos = current;
    setTooltip("tile.builder_pattern.flip");
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTilePatternSwap(tilePos, SwapType.POSITION));
    }
    return pressed;
  }
}
