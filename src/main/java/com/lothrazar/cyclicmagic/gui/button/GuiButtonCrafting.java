package com.lothrazar.cyclicmagic.gui.button;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.OpenCraftingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonCrafting extends GuiButton {
	private GuiScreen gui;
	public GuiButtonCrafting(GuiScreen g, int x, int y) {
		super(256, x, y, 10, 10, "C");
		gui = g;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			System.out.println("Gui name is "+this.gui.getClass());
			ModMain.network.sendToServer(new OpenCraftingPacket(new NBTTagCompound()));
		}

		return pressed;
	}
}
