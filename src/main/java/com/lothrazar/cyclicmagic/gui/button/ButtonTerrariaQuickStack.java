package com.lothrazar.cyclicmagic.gui.button;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.module.GuiTerrariaButtonsModule;
import com.lothrazar.cyclicmagic.net.PacketQuickStack;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTerrariaQuickStack extends GuiButton implements ITooltipButton {
  private List<String> tooltip = new ArrayList<String>();
  public ButtonTerrariaQuickStack(int buttonId, int x, int y) {
    super(buttonId, x, y, GuiTerrariaButtonsModule.BTNWIDTH, Const.btnHeight, "Q");
    tooltip.add(I18n.format("button.terraria.quickstack"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      // playerIn.displayGui(new BlockWorkbench.InterfaceCraftingTable(worldIn,
      // pos));
      ModMain.network.sendToServer(new PacketQuickStack(new NBTTagCompound()));
    }
    return pressed;
  }
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
}
