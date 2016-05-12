package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileWater extends BaseItemProjectile implements IHasRecipe {
 
	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this, 4), new ItemStack(Items.blaze_rod), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.ice));
		
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntityWaterBolt(world, player));
	}

}
