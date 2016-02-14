package com.lothrazar.cyclicmagic.config;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class IngameConfigHandler implements IModGuiFactory{

	@Override
	public void initialize(Minecraft mc){

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass(){

		return IngameConfigGui.class;// the only line we need to add
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories(){

		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element){

		return null;
	}
}
