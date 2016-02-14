package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellPhasing extends BaseSpell implements ISpell {

	public SpellPhasing(int id, String name) {
		super.init(id, name);
		this.cost = 30;
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos) {
		if (super.canPlayerCast(world, player, pos) == false) {
			return false;
		}

		BlockPos offs = getPosOffset(player, pos);

		if (pos != null && offs != null && world.isAirBlock(offs) && world.isAirBlock(offs.up())) {
			return true;
		}

		return false;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		if (pos == null) {
			return false;
		}// covered also by canPlayerCast

		BlockPos offs = getPosOffset(player, pos);// was .getOpposite()

		// not 2, depends on block pos?
		if (world.isAirBlock(offs) && world.isAirBlock(offs.up())) {

			player.setPositionAndUpdate(offs.getX(), offs.getY(), offs.getZ());

			this.spawnParticle(world, player, pos);
			this.playSound(world, null, pos);

			return true;
		}

		return false;
	}

	private BlockPos getPosOffset(EntityPlayer player, BlockPos pos) {
		if (pos == null) {
			return pos;
		}

		// this spell crashes servers its sideonly=client. TODO: what to do?
		EnumFacing face = EnumFacing.getFacingFromVector((float) player.getLookVec().xCoord, (float) player.getLookVec().yCoord, (float) player.getLookVec().zCoord);
		int dist = 1;
		if (face.getOpposite() == EnumFacing.DOWN) {
			dist = 2;// only move two when going down - player is 2 tall
		}
		// was .getOpposite()
		BlockPos offs = pos.offset(face, dist);
		return offs;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL, pos);

	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {
		UtilSound.playSound(world, pos, UtilSound.portal);

	}
}
