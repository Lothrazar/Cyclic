package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketSpellPush;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellRangePush extends BaseSpellRange {

	public SpellRangePush(int id, String name) {
		super.init(id, name);
	}

	@Override
	public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos pos, EnumFacing side) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new PacketSpellPush(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}

		return true;
	}

	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {

		BlockPos resultPosition = UtilPlaceBlocks.pushBlock(p.worldObj, p, pos, side);
		Block newSpot = null;

		if (resultPosition != null && p.worldObj.getBlockState(resultPosition) != null) {
			newSpot = p.worldObj.getBlockState(resultPosition).getBlock();
		}

		if (newSpot != null) {
			this.spawnParticle(p.worldObj, p, pos);
			this.playSound(p.worldObj,p, newSpot, pos);
		}
		// else it failed, nothing was moved
	}
}
