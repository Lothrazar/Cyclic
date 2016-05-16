package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFoodHeart extends ItemFood {

	public ItemFoodHeart() {
		super(2, false); 
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
 
		UtilEntity.incrementMaxHealth(player, 2);  
	} 
}
