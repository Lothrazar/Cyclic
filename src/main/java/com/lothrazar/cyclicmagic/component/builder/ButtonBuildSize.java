package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.net.PacketTileIncrementField;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildSize extends GuiButtonTooltip {
  private final BlockPos tilePos;
  boolean goUp;
  private String type;
  public ButtonBuildSize(BlockPos current, int buttonId, int x, int y, int width, boolean up, String strType) {
    super(buttonId, x, y, 15, 10, "");
    tilePos = current;
    goUp = up;
    type = TileEntityStructureBuilder.Fields.SIZE.name().toLowerCase();
    this.setTooltip("button." + type + "." + (goUp ? "up" : "down"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      int size = (goUp) ? 1 : -1;
      ModCyclic.network.sendToServer(new PacketTileIncrementField(tilePos, TileEntityStructureBuilder.Fields.SIZE.ordinal(), size));
    }
    return pressed;
  }
}
