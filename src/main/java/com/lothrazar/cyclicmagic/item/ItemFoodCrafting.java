package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilParticle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodCrafting extends ItemFood implements IHasRecipe,IHasConfig{
 
	private static final int numFood = 2;

	public ItemFoodCrafting() {
		super(numFood, false);  
		this.setMaxStackSize(1);
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {

		final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
		data.setInventoryCrafting(true);
		player.getEntityData().setBoolean("X", true);
		
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, player.getPosition());
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, player.getPosition().up());
		
		if(player.worldObj.isRemote){
			UtilChat.addChatMessage(player, "unlocks.crafting");
		}
	}

	@Override
	public void syncConfig(Configuration config) {

//		Property prop = config.get(Const.ConfigCategory.items, "HeartFood", true, "Edible hearts that increase your heath (permanently; until death)");
//		prop.setRequiresMcRestart(true);
//		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this) 
				,Blocks.CRAFTING_TABLE 
				,Items.PUMPKIN_PIE 
				,Items.CAKE
				,Items.COOKIE
				,new ItemStack(Items.FISH,1,Const.fish_salmon) 
				,Items.POISONOUS_POTATO
				);
	} 
}
