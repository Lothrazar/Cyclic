package com.lothrazar.samsmagic.potion;

import com.lothrazar.samsmagic.ItemRegistry;
import com.lothrazar.samsmagic.ModSpells;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCustom extends Potion
{ 
	protected PotionCustom(int potionID, ResourceLocation location,	boolean badEffect, int potionColor,ItemStack is) 
	{
		super(potionID, location, badEffect, potionColor); 
		this.setIconIndex(0, 0);
		icon = is;
	}
	private ItemStack icon;
	
	@Override
	public Potion setIconIndex(int par1, int par2) 
    {
        super.setIconIndex(par1, par2);
        return this;
    }
	
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon()
    {
        return false;//to block it from looking for one of the vanilla textures in the default way.
    }
    
	@Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) 
	{
		int border = 6;
		ModSpells.renderItemAt(icon, x + border, y + border, 16);
		/*
		@SuppressWarnings("deprecation")
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(icon);
		@SuppressWarnings("deprecation")
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());
		
		ModSpells.renderTexture( textureAtlasSprite, x + border, y + border, 16);
		*/
	
	}
}
