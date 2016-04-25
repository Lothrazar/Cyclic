package com.lothrazar.cyclicmagic.util;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UtilTextureRender {

	@SideOnly(Side.CLIENT)
	public static void drawTextureSimple(ResourceLocation res, int x, int y, int w, int h) {

		if (res == null) { return; }

		try {

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(res);
			Gui.drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, w, h, w, h);

		} catch (NullPointerException e) {

			ModMain.logger.error( "Null pointer drawTexture;Simple " + res.getResourcePath());
			ModMain.logger.error( e.getMessage());
			e.printStackTrace();

		} catch (net.minecraft.util.ReportedException e) {
			ModMain.logger.error("net.minecraft.util.ReportedException ");
			ModMain.logger.error(res.getResourceDomain() + ":" + res.getResourcePath());
			ModMain.logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawTextureSquare(ResourceLocation img, int x, int y, int dim) {

		drawTextureSimple(img, x, y, dim, dim);
	}
}