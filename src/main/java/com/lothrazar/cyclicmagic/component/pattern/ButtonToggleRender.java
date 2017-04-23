package com.lothrazar.cyclicmagic.component.pattern;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.pattern.PacketTilePatternSwap.SwapType;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonToggleRender extends GuiButton implements ITooltipButton {
  private final BlockPos tilePos;
  private final List<String> tooltips = new ArrayList<String>();
  public ButtonToggleRender(int buttonId, int x, int y, BlockPos current) {
    super(buttonId, x, y, 20, 20, "");
    tilePos = current;
    tooltips.add(UtilChat.lang("tile.builder_pattern.togglerender"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTilePatternSwap(tilePos, SwapType.RENDER));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
}
