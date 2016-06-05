package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileHarvest extends BaseItemProjectile implements IHasRecipe, IHasConfig{

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.items; 
		
		EntityHarvestBolt.range_main = config.getInt("Harvest Projectile Range", category, 6, 1, 32, "Horizontal range on level of hit to harvest");
		EntityHarvestBolt.range_offset = EntityHarvestBolt.range_main-2;//config.getInt("harvest.rangeOffset", category, 4, 1, 32, "Horizontal range on further heights to harvest");
		
		//TODO: since util is shared with other features, need to revisit this
		//EntityHarvestBolt.doesHarvestStem = config.getBoolean("harvest.does_harvest_stem", category, false, "Does it harvest stems (pumkin/melon)");
		//EntityHarvestBolt.doesHarvestSapling = config.getBoolean("harvest.does_harvest_sapling", category, false, "Does it harvest sapling");
		//EntityHarvestBolt.doesHarvestTallgrass = config.getBoolean("harvest.does_harvest_tallgrass", category, false, "Does it harvest tallgrass/doubleplants");
		//EntityHarvestBolt.doesHarvestMushroom = config.getBoolean("harvest.does_harvest_mushroom", category, true, "Does it harvest mushrooms");
		//EntityHarvestBolt.doesMelonBlocks = config.getBoolean("harvest.does_harvest_melonblock", category, true, "Does it harvest pumpkin block");
		//EntityHarvestBolt.doesPumpkinBlocks = config.getBoolean("harvest.does_harvest_pumpkinblock", category, true, "Does it harvest melon block");

	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this, 4), new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.WHEAT), new ItemStack(Items.WHEAT_SEEDS));
		
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntityHarvestBolt(world, player));
	}

}
