package com.lothrazar.cyclicmagic.component.cyclicwand;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketWandGui;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonWandReset extends GuiButton implements ITooltipButton {
  //  private final EntityPlayer thePlayer;
  public ButtonWandReset(EntityPlayer p, int buttonId, int x, int y, int width) {
    super(buttonId, x, y, width, 20, UtilChat.lang("button.reset.name"));
    //    thePlayer = p;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketWandGui(PacketWandGui.WandAction.RESET));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    List<String> tooltips = new ArrayList<String>();// import net.minecraft.client.resources.I18n;
    tooltips.add(UtilChat.lang("button.reset.tooltip"));
    return tooltips;
  }
}
