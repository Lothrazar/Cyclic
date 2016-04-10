package com.lothrazar.cyclicmagic.util;

import org.apache.logging.log4j.Level;
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

			ModMain.logger.log(Level.ERROR, "Null pointer drawTexture;Simple " + res.getResourcePath());
			ModMain.logger.log(Level.ERROR, e.getMessage());
			ModMain.logger.log(Level.ERROR, e.getStackTrace());

		} catch (net.minecraft.util.ReportedException e) {
			ModMain.logger.log(Level.ERROR, "net.minecraft.util.ReportedException ");
			ModMain.logger.log(Level.ERROR, res.getResourceDomain() + ":" + res.getResourcePath());
			ModMain.logger.log(Level.ERROR, e.getMessage());
			ModMain.logger.log(Level.ERROR, e.getStackTrace());
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawTextureSquare(ResourceLocation img, int x, int y, int dim) {

		drawTextureSimple(img, x, y, dim, dim);
	}
}