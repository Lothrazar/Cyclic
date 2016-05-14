package com.lothrazar.cyclicmagic.gui.button;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketQuickStack;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonTerrariaQuickStack extends GuiButton {
	public ButtonTerrariaQuickStack(int buttonId, int x, int y) {
		super(buttonId, x, y, Const.btnWidth, Const.btnHeight, I18n.translateToLocal("btn.quickstack"));
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
}
