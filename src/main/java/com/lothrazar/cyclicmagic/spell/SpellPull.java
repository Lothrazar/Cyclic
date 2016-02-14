package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellPull;
import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SpellPull extends BaseSpell {
	public SpellPull(int id,String name){
		super.init(id,name);
		this.cooldown = 1;
		this.cost = 1;
	}
	int maxRange = 64;// TODO: config

	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side ) {

		//TODO: we are basically replacing these with the mouseover versions... how to genericify these?
		/*
		if(side == null || world.getBlockState(pos) == null){
			return false;
		}*/
		
		if (!p.capabilities.allowEdit) {
			return false;
		}

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellPull(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}
		
		return false;
	}

	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {

		//BlockPos resultPosition = 
		UtilMoveBlock.pullBlock(p.worldObj, p, pos, side);
		
		this.spawnParticle(p.worldObj, p, pos);
		this.playSound(p.worldObj, null, pos);
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
		
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {

		UtilSound.playSound(world, pos, UtilSound.Own.bwoaaap );
	}
}
