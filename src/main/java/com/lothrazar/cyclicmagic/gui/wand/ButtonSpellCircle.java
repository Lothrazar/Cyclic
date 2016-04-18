package com.lothrazar.cyclicmagic.gui.wand;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonSpellCircle extends GuiButton implements ITooltipButton {

	public ButtonSpellCircle(int id, int x, int y, int width) {

		super(id, x, y, width, 20, "?");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			mc.thePlayer.closeScreen();

			// this only works on the clientside anwyway :)
			ModMain.proxy.displayGuiSpellbook();
		}

		return pressed;
	}

	@Override
	public List<String> getTooltips() {
		List<String> tooltips = new ArrayList<String>();

		tooltips.add(net.minecraft.util.text.translation.I18n.translateToLocal("button.circle"));

		return tooltips;
	}
}
