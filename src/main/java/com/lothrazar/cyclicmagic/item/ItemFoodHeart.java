package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodHeart extends ItemFood implements IHasRecipe,IHasConfig{
 
	public ItemFoodHeart() {
		super(2, false); 
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
 
		UtilEntity.incrementMaxHealth(player, 2);  
	}

	@Override
	public void syncConfig(Configuration config) {

		Property prop = config.get(Const.ConfigCategory.items, "HeartFood", true, "Edible hearts that increase your heath (permanently, until death");
		prop.setRequiresMcRestart(true);
		ItemRegistry.setConfigMap(this,prop.getBoolean());
		
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this) 
				,Items.beetroot
				,Items.rabbit_stew
				,Items.pumpkin_pie
				,Items.mushroom_stew
				,Items.cake
				,Items.cooked_fish
				,Items.cooked_rabbit
				,Items.golden_apple
				,Items.speckled_melon
				);
	} 
}
