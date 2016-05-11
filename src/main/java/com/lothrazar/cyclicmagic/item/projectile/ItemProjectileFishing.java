package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.registry.ItemRegistry.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileFishing extends BaseItemProjectile implements IHasRecipe, IHasConfig{

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this, 8)
				, new ItemStack(Items.ender_pearl), new ItemStack(Items.gunpowder), new ItemStack(Items.string));
		
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntityFishingBolt(world, player));
	}

}
