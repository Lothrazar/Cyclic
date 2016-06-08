package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellPlaceCircle extends BaseSpellPlace {

	public SpellPlaceCircle(int id, String name) {
		super.init(id, name);
	}

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {

		// JUST for the circle, we ignore clicked pos and always start on players
		// position
		BlockPos startPos = player.getPosition();

		System.out.println("circle at "+startPos.toString());
		
		UtilPlaceBlocks.circle(world, player, wand, startPos);

		return true;
	}

}
