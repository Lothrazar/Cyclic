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

public class ButtonBuilderType extends GuiButton implements ITooltipButton {
  private final BlockPos tilePos;
  private final List<String> tooltips = new ArrayList<String>();
  public ButtonBuilderType(BlockPos current, int buttonId, int x, int y, int width) {
    super(buttonId, x, y, width, 20, "");
    tilePos = current;
    tooltips.add(TextFormatting.GRAY + UtilChat.lang("button.builder.meta"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileBuildType(tilePos));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
}
