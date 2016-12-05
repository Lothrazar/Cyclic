package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle.ActionType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonSizePreview extends GuiButtonTooltip {
  private BlockPos tilePos;
  private ActionType type;
  public GuiButtonSizePreview(int buttonId, int x, int y, String buttonText, BlockPos p, PacketTileSizeToggle.ActionType t) {
    super(buttonId, x, y, 44, 20, buttonText);
    this.tilePos = p;
    this.type = t;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileSizeToggle(tilePos, type));
    }
    return pressed;
  }
}
