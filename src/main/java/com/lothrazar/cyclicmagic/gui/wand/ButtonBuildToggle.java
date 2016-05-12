package com.lothrazar.cyclicmagic.gui.wand;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageToggleBuild;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildToggle extends GuiButton implements ITooltipButton {

	final EntityPlayer thePlayer;// TODO: could store player instead of wand

	public ButtonBuildToggle(EntityPlayer player, int buttonId, int x, int y, int width) {

		super(buttonId, x, y, width, 20, "");
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {

			ModMain.network.sendToServer(new MessageToggleBuild());
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

		ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(thePlayer);
		this.displayString = I18n.translateToLocal(ItemCyclicWand.BuildType.getName(wand));

		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public List<String> getTooltips() {

		List<String> tooltips = new ArrayList<String>();
		// tooltips.add(I18n.translateToLocal("button.build.tooltip"));
		ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(thePlayer);
		String key = ItemCyclicWand.BuildType.getName(wand) + ".tooltip";
		tooltips.add(I18n.translateToLocal(key));
		tooltips.add(TextFormatting.GRAY + I18n.translateToLocal("button.build.meta"));

		return tooltips;
	}
}
