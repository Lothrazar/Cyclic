package com.lothrazar.cyclicmagic.gui;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle.ActionType;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonToggleSize extends GuiButtonTooltip {
  private BlockPos tilePos;
  public GuiButtonToggleSize(int buttonId, int x, int y,  BlockPos p) {
    super(buttonId, x, y, 44, 20, "");
    this.tilePos = p;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileSizeToggle(tilePos, ActionType.SIZE));
    }
    return pressed;
  }
}
