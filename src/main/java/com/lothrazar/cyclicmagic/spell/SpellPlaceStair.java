package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellPlaceStair extends BaseSpellPlace{

	public SpellPlaceStair(int id, String name){

		super.init(id, name);
		this.cost = 25;
		this.cooldown = 10;
	}
	
	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand,BlockPos pos, EnumFacing side) {

		BlockPos startPos = pos;
		if(startPos == null){
			startPos = player.getPosition();
		}
		else{
			//we have a valid start position that was clicked on.  was the face of a block clicked on too?
			startPos = startPos.offset(side);
		}

		UtilPlaceBlocks.stairway(world, player, wand,startPos );
		
		return false;
	}
}
