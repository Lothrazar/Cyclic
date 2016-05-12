package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellPlaceLine extends BaseSpellPlace {

	public SpellPlaceLine(int id, String name) {

		super.init(id, name);
		this.cost = 25;
		this.cooldown = 15;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub

		BlockPos startPos = pos;
		if (startPos == null) {
			startPos = player.getPosition();
		}
		else {
			// we have a valid start position that was clicked on. was the face of a
			// block clicked on too?
			startPos = startPos.offset(side);
		}

		if(side == null){
			return false;//it means block was not clicked on, it was clicked in midair
		} 

		// boolean isLookingUp = (player.getLookVec().yCoord >= 0);//TODO: use this
		// somehow? to place up/down?

		UtilPlaceBlocks.line(world, player, wand, startPos, UtilEntity.getPlayerFacing(player));

		return true;
	}
}
