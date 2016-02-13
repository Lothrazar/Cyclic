package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRotate extends BaseSpell {
	public SpellRotate(int id, String name) {
		super.init(id, name);
		this.cooldown = 1;
		this.cost = 2;
	}

	int maxRange = 64;// TODO: config

	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side) {

		if (!p.capabilities.allowEdit) {
			return false;
		}

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellRotate(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {
		// TODO Auto-generated method stub
		if (pos == null || p.worldObj.getBlockState(pos) == null || side == null) {
			return false;
		}

		IBlockState clicked = p.worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) {
			return false;
		}

		boolean isDone = false;

		if (clicked.getBlock().rotateBlock(p.worldObj, pos, side)) {
			// for example, BlockMushroom.rotateBlock uses this, and hay bales
			// use it to swap the 'axis'
			isDone = true;
		}
		else {
			// any property that is not variant?
			for (IProperty prop : (java.util.Set<IProperty>) clicked.getProperties().keySet()) {
				// since slabs do not use rotateBlock, swap the up or down half
				// being used
				if (prop.getName().equals("half")) {
					p.worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
				}
			}
		}

		if (isDone && clicked.getBlock().stepSound != null && clicked.getBlock().stepSound.getPlaceSound() != null) {
			UtilSound.playSoundAt(p, clicked.getBlock().stepSound.getPlaceSound());
		}
		return isDone;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}
}
