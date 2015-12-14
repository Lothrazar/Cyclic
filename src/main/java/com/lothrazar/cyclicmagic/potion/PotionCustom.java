package com.lothrazar.cyclicmagic.potion;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCustom extends Potion
{ 
	public PotionCustom(int potionID, ResourceLocation location,	boolean badEffect, int potionColor) 
	{
		super( location, badEffect, potionColor); 
		//TODO: WTF no id?
		System.out.println("potion id deprec "+potionID);
		this.setIconIndex(0, 0);
	}
	
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
    /*
	@Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) 
	{
		int border = 6;
		UtilTextureRender.renderItemAt(icon, x + border, y + border, 16);

	
	}
	*/
}
