package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRotate extends BaseSpell {
	public SpellRotate(int id,String name){
		super(id,name);
		this.cooldown = 1;
		this.cost = 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if (pos == null || world.getBlockState(pos) == null || side == null) {
			return false;
		}
		
		IBlockState clicked = world.getBlockState(pos);
		if(clicked.getBlock()==null){
			return false;
		}
		
		boolean isDone = false;
		
		if (clicked.getBlock().rotateBlock(world, pos, side)) {
			// for example, BlockMushroom.rotateBlock uses this, and hay bales
			// use it to swap the 'axis'
			isDone = true;
		} else {
			// any property that is not variant?
			for (IProperty prop : (java.util.Set<IProperty>) clicked.getProperties().keySet()) {
				// since slabs do not use rotateBlock, swap the up or down half
				// being used
				if (prop.getName().equals("half")) {
					world.setBlockState(pos, clicked.cycleProperty(prop));
					
					isDone = true;
				}
			}
		}
		
		if(isDone && clicked.getBlock().stepSound != null && clicked.getBlock().stepSound.getPlaceSound() != null){
			UtilSound.playSoundAt(player, clicked.getBlock().stepSound.getPlaceSound());
		}
		
		return isDone;
	}
}
