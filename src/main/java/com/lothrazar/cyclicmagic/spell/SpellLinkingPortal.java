package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.item.ItemWaypointPortal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellLinkingPortal  extends BaseSpell implements ISpell {
//on use, spawns an item (purple orb texture maybe)
	//that saves its current coordinates. acts like a one time use linking book.
	//so that when you use it, you return to that exact spot
	//also put on display the coords,biome, player who created it
	public SpellLinkingPortal(int id,String name){
		super(id,name);
		this.cost = 35;
	}
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {
		
		ItemStack drop = new ItemStack(ItemRegistry.waypoint_portal);
		
		ItemWaypointPortal.saveCurrentLocation(player, drop);
		 
		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),drop));
		}
		
		return false;
	}
}
