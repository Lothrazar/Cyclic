package com.lothrazar.cyclicmagic.potion;

import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCustom extends Potion {

	private ResourceLocation icon;

	public PotionCustom(ResourceLocation location, boolean badEffect, int potionColor, String nameIn) {

		super(badEffect, potionColor);

		this.setIcon(location);
		this.setPotionName(nameIn);
	}
/*
	@Override
	public Potion setIconIndex(int par1, int par2) {

		super.setIconIndex(par1, par2);
		return this;
	}
*/
	private final boolean showsInTopRight = false;
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() {
//id love to ACTUALLY show it, but the resource location doesnt get used
		return showsInTopRight;// to block it from looking for one of the vanilla textures
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {

		int border = 6;
		UtilTextureRender.drawTextureSquare(getIcon(), x + border, y + border, 16);
	}
	public ResourceLocation getIcon() {
		return icon;
	}
	public void setIcon(ResourceLocation icon) {
		this.icon = icon;
	}
}