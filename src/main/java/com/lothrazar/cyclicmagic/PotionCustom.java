package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCustom extends Potion{

	private ResourceLocation icon;

	@SuppressWarnings("deprecation")
	public PotionCustom(int potionID, ResourceLocation location, boolean badEffect, int potionColor, String nameIn){

		super(potionID, location, badEffect, potionColor);

		icon = location;
		this.setPotionName(nameIn);
	}

	@Override
	public Potion setIconIndex(int par1, int par2){

		super.setIconIndex(par1, par2);
		return this;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon(){

		return false;// to block it from looking for one of the vanilla textures
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc){

		int border = 6;
		UtilTextureRender.drawTextureSquare(icon, x + border, y + border, 16);
	}
}