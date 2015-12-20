package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ItemRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellGiveLauncher extends BaseSpell implements ISpell {
	public SpellGiveLauncher(){
		super();
		this.cooldown = 60;
		this.durability = 1000;
		this.experience = 1000;
	}
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		ItemStack drop = new ItemStack(ItemRegistry.launch_wand);
 
		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),drop));
		}
		return false;
	}
}
