package com.lothrazar.cyclicmagic.gui;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.GuiTerrariaButtonsModule;
import com.lothrazar.cyclicmagic.net.PacketQuickStack;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTerrariaQuickStack extends GuiButton implements ITooltipButton {
  private List<String> tooltip = new ArrayList<String>();
  public ButtonTerrariaQuickStack(int buttonId, int x, int y) {
    super(buttonId, x, y, GuiTerrariaButtonsModule.BTNWIDTH, Const.btnHeight, "Q");
    tooltip.add(UtilChat.lang("button.terraria.quickstack"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      // playerIn.displayGui(new BlockWorkbench.InterfaceCraftingTable(worldIn,
      // pos));
      ModCyclic.network.sendToServer(new PacketQuickStack(new NBTTagCompound()));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
}
