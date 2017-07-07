package com.lothrazar.cyclicmagic.component.cyclicwand;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildToggle extends GuiButton implements ITooltipButton {
  private final EntityPlayer thePlayer;
  public ButtonBuildToggle(EntityPlayer p, int buttonId, int x, int y, int width) {
    super(buttonId, x, y, width, 20, "");
    thePlayer = p;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketWandGui(PacketWandGui.WandAction.BUILDTYPE));
    }
    return pressed;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float p) {
    this.displayString = UtilChat.lang(ItemCyclicWand.BuildType.getName(UtilSpellCaster.getPlayerWandIfHeld(thePlayer)));
    super.drawButton(mc, mouseX, mouseY, p);
  }
  @Override
  public List<String> getTooltips() {
    List<String> tooltips = new ArrayList<String>();
    String key = ItemCyclicWand.BuildType.getName(UtilSpellCaster.getPlayerWandIfHeld(thePlayer)) + ".tooltip";
    tooltips.add(UtilChat.lang(key));
    return tooltips;
  }
}
