package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangeRotate extends BaseSpellRange {

	public SpellRangeRotate(int id, String name) {

		super.init(id, name);
		this.cost = 35;
		this.cooldown = 2;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos pos, EnumFacing side) {

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
	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {

		// TODO Auto-generated method stub
		if (pos == null || p.worldObj.getBlockState(pos) == null || side == null) { return; }

		IBlockState clicked = p.worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) { return; }

		boolean isDone = false;

		if (clicked.getBlock().rotateBlock(p.worldObj, pos, side)) {
			// for example, BlockMushroom.rotateBlock uses this, and hay bales
			// use it to swap the 'axis'
			isDone = true;
		}
		else {
			// any property that is not variant?

			for (IProperty prop : (com.google.common.collect.ImmutableSet<IProperty<?>>) clicked.getProperties().keySet()) {
				// for(IProperty prop : (java.util.Set<IProperty>)
				// clicked.getProperties().keySet()){
				// since slabs do not use rotateBlock, swap the up or down half
				// being used

				if (prop.getName().equals("half")) {

					p.worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
					break;
				}
			}
		}

		if (isDone) {
			this.playSound(p.worldObj, clicked.getBlock(), pos);
			this.spawnParticle(p.worldObj, p, pos);
		}
		return;
	}
}
