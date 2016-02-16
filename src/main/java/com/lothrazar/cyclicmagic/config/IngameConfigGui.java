package com.lothrazar.cyclicmagic.config;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class IngameConfigGui extends GuiConfig{

	public IngameConfigGui(GuiScreen parent){

		super(parent, new ConfigElement(ModMain.cfg.instance().getCategory(Const.MODID)).getChildElements(), Const.MODID, false, false, "Cyclic Magic");
		this.titleLine2 = "See the " + Const.MODID + ".cfg file for even more options";
	}

	@Override
	public void initGui(){

		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button){

		super.actionPerformed(button);
	}
}
