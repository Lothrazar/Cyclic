package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellPush;
import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangePush extends BaseSpellRange {
	public SpellRangePush(int id,String name){
		super.init(id,name);
		this.cooldown = 1;
		this.cost = 1;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side ) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellPush(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}
		
		return true;
	}

	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {
		
		//BlockPos resultPosition = 
		UtilMoveBlock.pushBlock(p.worldObj, p, pos, side);

		this.spawnParticle(p.worldObj, p, pos);
		this.playSound(p.worldObj, null, pos);
	}
}
