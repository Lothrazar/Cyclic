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

			//set it in client side and server both
			TileEntityBuilder.BuildType old = container.getBuildTypeEnum();
			TileEntityBuilder.BuildType next = TileEntityBuilder.BuildType.getNextType(old);
			container.setBuildType(next);
			
			ModMain.network.sendToServer(new PacketTileBuildType(container.getPos(),next));

			container.markDirty();
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
	this.displayString = I18n.format("builder."+container.getBuildType()+".name");
//		
//		if(container.getBuildType() != null)
//			this.displayString = I18n.format(container.getBuildType().toString().toLowerCase()+".name");
//		else{
//			System.out.println("build type null i guess??");
//		}
		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public List<String> getTooltips() {

		List<String> tooltips = new ArrayList<String>();

//		String key = container.getBuildType().toString().toLowerCase() + ".tooltip";
//		tooltips.add(I18n.format(key));
//		tooltips.add(TextFormatting.GRAY + I18n.format("button.build.meta"));

		
		return tooltips;
	}
}
