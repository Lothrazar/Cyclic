package com.lothrazar.cyclicmagic.gui.button;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.event.EventGuiTerrariaButtons;
import com.lothrazar.cyclicmagic.net.PacketDepositPlayerToNearby;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.resources.I18n;
//net.minecraft.client.resources.I18n
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTerrariaDepositAll extends GuiButton implements ITooltipButton {
  private List<String> tooltip = new ArrayList<String>();
  public ButtonTerrariaDepositAll(int buttonId, int x, int y) {
    super(buttonId, x, y, EventGuiTerrariaButtons.BTNWIDTH, Const.btnHeight, "D");
    tooltip.add(I18n.format("button.terraria.deposit"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModMain.network.sendToServer(new PacketDepositPlayerToNearby(new NBTTagCompound()));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
}
