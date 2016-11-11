package com.lothrazar.cyclicmagic.gui.harvester;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.net.PacketTileHarvester;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonHarvester extends GuiButtonTooltip {
  private BlockPos tilePos;
  public GuiButtonHarvester(int buttonId, int x, int y,  String buttonText
      , BlockPos p) {
    super(buttonId, x, y, 40, 20, buttonText);
    this.tilePos = p;
  }  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
     ModCyclic.network.sendToServer(new PacketTileHarvester(tilePos));
    }
    return pressed;
  }
}
