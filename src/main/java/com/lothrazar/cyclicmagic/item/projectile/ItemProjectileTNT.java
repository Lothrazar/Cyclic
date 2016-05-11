package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileTNT extends BaseItemProjectile implements IHasRecipe{

	private int strength;

	public ItemProjectileTNT(int str){
		super();
		this.strength = str;
	}
	
	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntityDynamite(world, player,strength));
	}

	@Override
	public void addRecipe() {
		
		switch(strength){
		case 1:
			GameRegistry.addShapelessRecipe(new ItemStack(this)
					, new ItemStack(Items.ender_pearl)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.clay_ball));
			break;
		case 2:
			GameRegistry.addShapelessRecipe(new ItemStack(this)
					, new ItemStack(Items.ender_pearl)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.clay_ball));
			break;
		case 3:
			GameRegistry.addShapelessRecipe(new ItemStack(this)
					, new ItemStack(Items.ender_pearl)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.clay_ball));
			break;
		case 4:
			GameRegistry.addShapelessRecipe(new ItemStack(this)
					, new ItemStack(Items.ender_pearl)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.gunpowder)
					, new ItemStack(Items.clay_ball));

			break;
		}
	}
}
