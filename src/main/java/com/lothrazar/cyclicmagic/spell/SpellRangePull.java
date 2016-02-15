package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellPull;
import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangePull extends BaseSpellRange{

	public SpellRangePull(int id, String name){

		super.init(id, name);
		this.cooldown = 8;
		this.cost = 5;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side){

		if(world.isRemote){

			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if(mouseover != null){
				ModMain.network.sendToServer(new MessageSpellPull(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}

		return true;
	}

	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p){

		BlockPos resultPosition = UtilMoveBlock.pullBlock(p.worldObj, p, pos, side);
		
		Block newSpot = null;
		if(resultPosition != null && p.worldObj.getBlockState(resultPosition) != null){
			newSpot = p.worldObj.getBlockState(resultPosition).getBlock();
		}
		
		if(newSpot != null){
			this.spawnParticle(p.worldObj, p, pos);
			this.playSound(p.worldObj, newSpot, pos);
		}
		//else it failed, nothing was moved
	}
}
