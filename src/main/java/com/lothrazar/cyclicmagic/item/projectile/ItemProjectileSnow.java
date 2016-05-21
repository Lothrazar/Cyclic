package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileSnow extends BaseItemProjectile implements IHasRecipe, IHasConfig{

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.items_projectiles; 
		EntitySnowballBolt.damageEntityOnHit = config.getBoolean("snow.knockback", category, true, "Does it damage entity or not on hit (1 damage to blaze, 0 to others)");
		
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this, 6), new ItemStack(Items.ENDER_PEARL), new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
		
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntitySnowballBolt(world, player));
	}

}
