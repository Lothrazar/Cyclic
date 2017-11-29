package com.lothrazar.cyclicmagic.gui.button;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.net.PacketTileFuelDisplay;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonToggleFuelBar extends GuiButtonTooltip {
  private BlockPos tilePos;
  public GuiButtonToggleFuelBar(int buttonId, int x, int y, BlockPos p) {
    super(buttonId, x, y, 7, 7, "");//44, 20,
    this.tilePos = p;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileFuelDisplay(tilePos));
    }
    return pressed;
  }
  public void setState(boolean onState) {
    if (onState)
      this.setTooltips(Arrays.asList(UtilChat.lang("energy.button.top")));
    else
      this.setTooltips(Arrays.asList(UtilChat.lang("energy.button.side")));
  }
}
