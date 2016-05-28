package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileSnow extends BaseItemProjectile implements IHasRecipe{

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(this, 6), new ItemStack(Items.ENDER_PEARL), new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
		
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntitySnowballBolt(world, player));
	}
}
