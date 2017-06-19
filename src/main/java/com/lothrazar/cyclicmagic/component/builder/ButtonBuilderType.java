package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuilderType extends GuiButtonTooltip {
  private final BlockPos tilePos;
  public ButtonBuilderType(BlockPos current, int buttonId, int x, int y, int width) {
    super(buttonId, x, y, width, 20, "");
    tilePos = current;
    this.setTooltip("button.builder.tooltip");
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
}
