package com.lothrazar.cyclicmagic.component.controlledminer;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonMinerHeight extends GuiButtonTooltip {
  private final BlockPos tilePos;
  boolean goUp;
  private String stype;//TODO: should be field ordinal/int not string but meh
  public ButtonMinerHeight(BlockPos current, int buttonId, int x, int y, boolean up, TileEntityControlledMiner.Fields list) {
    super(buttonId, x, y, 14, 14, "");
    tilePos = current;
    goUp = up;
    stype = list.name().toLowerCase();
    setTooltip("button." + stype + "." + (goUp ? "up" : "down"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      int size = (goUp) ? 1 : -1;
      ModCyclic.network.sendToServer(new PacketTileMineHeight(tilePos, size, stype));
    }
    return pressed;
  }
}
