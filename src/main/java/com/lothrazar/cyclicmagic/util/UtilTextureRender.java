package com.lothrazar.cyclicmagic.util;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UtilTextureRender{

	public static void drawTextureSimple(ResourceLocation res, int x, int y, int w, int h){

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, w, h, w, h);
	}

	@SideOnly(Side.CLIENT)
	public static void drawTextureSquare(ResourceLocation img, int x, int y, int dim){

		drawTextureSimple(img, x, y, dim, dim);
	}
}