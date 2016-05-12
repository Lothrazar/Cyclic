package com.lothrazar.cyclicmagic.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UtilItemRenderer{

	@SideOnly(Side.CLIENT)
	public static void renderItemAt(ResourceLocation res, int x, int y){

		int w = 16, h = 16;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, w, h, w, h);

	}

	@SideOnly(Side.CLIENT)
	public static void renderItemCurrentGui(ItemStack stack, int x, int y, int dim){

		// first get texture from item stack
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);

		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getParticleTexture().getIconName());

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

		if(Minecraft.getMinecraft().currentScreen != null){
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, textureAtlasSprite, dim, dim);
		}
	}
}
