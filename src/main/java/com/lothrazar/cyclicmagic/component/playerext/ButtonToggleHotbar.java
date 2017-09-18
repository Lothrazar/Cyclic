package com.lothrazar.cyclicmagic.component.playerext;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.net.PacketSwapPlayerHotbar;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ButtonToggleHotbar extends GuiButtonTooltip {
  private int row;
  public ButtonToggleHotbar(int id, int x, int y, int w, int h, int row) {
    super(id, x, y, w, h, "");
    this.row = row;
    this.setTooltip("button.inventory.hotbarswap");
  }
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketSwapPlayerHotbar(this.row));
    }
    return pressed;
  }
}
