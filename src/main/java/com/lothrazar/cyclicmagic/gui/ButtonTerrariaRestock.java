package com.lothrazar.cyclicmagic.gui;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.GuiTerrariaButtonsModule;
import com.lothrazar.cyclicmagic.net.PacketRestockContainerToPlayer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTerrariaRestock extends GuiButton implements ITooltipButton {
  private List<String> tooltip = new ArrayList<String>();
  public ButtonTerrariaRestock(int buttonId, int x, int y) {
    super(buttonId, x, y, GuiTerrariaButtonsModule.BTNWIDTH, Const.btnHeight, "R");
    tooltip.add(UtilChat.lang("button.terraria.restock"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketRestockContainerToPlayer(new NBTTagCompound()));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
}
