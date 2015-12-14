package com.lothrazar.cyclicmagic.util;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UtilTextureRender {
	
	/*
	@SideOnly(Side.CLIENT)
	public static void renderItemAt(ItemStack stack, int x, int y, int dim) {
		// first get texture from item stack
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

		if (Minecraft.getMinecraft().currentScreen != null)
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, textureAtlasSprite, dim, dim);
	}*/

	public static void drawTextureSimple(ResourceLocation res, int x, int y, int w, int h) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, w, h, w, h);
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderItemAt(ItemStack stack, int x, int y, int dim) {
		// first get texture from item stack
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		//Minecraft.getMinecraft().
		if (Minecraft.getMinecraft().ingameGUI != null){
			Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, textureAtlasSprite, dim, dim);
		}
	}
	@SideOnly(Side.CLIENT)
	public static void drawTextureSquare(ResourceLocation img, int x, int y, int dim) {

		drawTextureSimple(img,x,y,dim,dim);
		/*
		Minecraft.getMinecraft().getTextureManager().bindTexture(img);
		
		// int x, int y, int textureX, int textureY, int width, int height
		if (Minecraft.getMinecraft().ingameGUI != null){
			Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 0, 0, dim, dim);
		}
		*/
	}
}