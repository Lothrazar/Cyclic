package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellPull;
import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellPull extends BaseSpell {
	public SpellPull(int id,String name){
		super(id,name);
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
		

		//it returns a position if it was moved -> the pos it was moved to
		return false;// (resultPosition != null);
	}

	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {

		//BlockPos resultPosition = 
		UtilMoveBlock.pullBlock(p.worldObj, p, pos, side);
		
		//TODO: sounds/etc at pos
	}
}
