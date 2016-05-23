package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.event.EventNoclipUpdate;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodCorruptedChorus extends ItemFood implements IHasRecipe,IHasConfig{
 
	private static final int numFood = 2;
	
	public ItemFoodCorruptedChorus() {
		super(numFood, false);  
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
 
		EventNoclipUpdate.setPlayerGhostMode(player, world);
	}

	@Override
	public void syncConfig(Configuration config) {

		Property prop = config.get(Const.ConfigCategory.items, "CorruptedChorus", true, "Lets you phase through walls for a few seconds");
		prop.setRequiresMcRestart(true);
		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}

	@Override
	public void addRecipe(){

		GameRegistry.addRecipe(new ItemStack(this,3), 
				"lal", "lal", "lal", 
				'l', Items.FERMENTED_SPIDER_EYE, 
				'a', Items.CHORUS_FRUIT);
	} 
}
