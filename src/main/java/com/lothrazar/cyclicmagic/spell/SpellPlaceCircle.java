package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellPlaceCircle extends BaseSpellPlace{

	public SpellPlaceCircle(int id, String name){

		super.init(id, name);
		this.cost = 25;
		this.cooldown = 10;
	}
	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand,BlockPos pos, EnumFacing side) {

		//JUST for the circle, we ignore clicked pos and always start on players position
		BlockPos startPos = player.getPosition();
		
		UtilPlaceBlocks.circle(world, player, wand,startPos, ItemCyclicWand.BuildType.getBuildSize(wand) );
		
		return false;
	}

}
