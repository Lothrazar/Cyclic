package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellGhost extends BaseSpell{

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		// https://github.com/PrinceOfAmber/Cyclic/blob/2c947d4a91c28f8f853c6d1171ad019002892972/src/main/java/com/lothrazar/cyclicmagic/spell/SpellGhost.java
		
		
		return false;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(World world, EntityPlayer player, Block block, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}

}
