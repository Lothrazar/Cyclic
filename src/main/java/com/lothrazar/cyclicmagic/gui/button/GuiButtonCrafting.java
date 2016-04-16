package com.lothrazar.cyclicmagic.gui.button;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.OpenCraftingPacket;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonCrafting extends GuiButton {
	public GuiButtonCrafting( int x, int y) {
		super(256, x, y, 10, Const.btnHeight, I18n.translateToLocal("btn.crafting"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			ModMain.network.sendToServer(new OpenCraftingPacket(new NBTTagCompound()));
		}

		return pressed;
	}
}
