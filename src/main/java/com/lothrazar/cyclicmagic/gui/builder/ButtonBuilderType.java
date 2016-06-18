package com.lothrazar.cyclicmagic.gui.builder;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTileBuildType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuilderType extends GuiButton implements ITooltipButton {

	final TileEntityBuilder container;

	public ButtonBuilderType(TileEntityBuilder current, int buttonId, int x, int y, int width) {

		super(buttonId, x, y, width, 20, "");
		container = current;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {


			
			ModMain.network.sendToServer(new PacketTileBuildType(container.getPos()));

//			container.markDirty();
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
	this.displayString = I18n.format("builder.toggle.name");

		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public List<String> getTooltips() {

		List<String> tooltips = new ArrayList<String>();

		tooltips.add(TextFormatting.GRAY + I18n.format("button.build.meta"));

		
		return tooltips;
	}
}
