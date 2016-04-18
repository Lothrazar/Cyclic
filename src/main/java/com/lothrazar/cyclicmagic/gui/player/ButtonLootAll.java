package com.lothrazar.cyclicmagic.gui.player;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.LootAllPacket;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonLootAll extends GuiButton {
	public ButtonLootAll(int buttonId, int x, int y) {
		super(buttonId, x, y, Const.btnWidth, Const.btnHeight, I18n.translateToLocal("btn.lootall"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			ModMain.network.sendToServer(new LootAllPacket(new NBTTagCompound()));
		}

		return pressed;
	}
}
