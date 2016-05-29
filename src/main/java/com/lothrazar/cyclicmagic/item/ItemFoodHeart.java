package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodHeart extends ItemFood implements IHasRecipe,IHasConfig{
 
	private static final int numFood = 2;
	private static final int numHearts = 1;
	
	public ItemFoodHeart() {
		super(numFood, false);  
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
 
		//one heart is 2 health points (half heart = 1 health)
		UtilEntity.incrementMaxHealth(player, 2 * numHearts);  
	}
//Edible hearts that increase your heath (permanently; until death)
	@Override
	public void syncConfig(Configuration config) {

//TODO: get hearts persisting through death config to wipe on death?
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this) 
				,Items.BEETROOT
				,Items.RABBIT
				,Items.PUMPKIN_PIE
				,Items.DIAMOND
				,Items.CAKE
				,Items.COOKIE
				,new ItemStack(Items.FISH,1,Const.fish_salmon)
				,Items.GOLDEN_APPLE
				,Items.POISONOUS_POTATO
				);
	} 
}
