package com.lothrazar.cyclicmagic.spell;

import java.util.List;
import com.lothrazar.cyclicmagic.util.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SpellCollect extends BaseSpellExp {

	private final int radius = 20;
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos) {
	
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		
		List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.fromBounds(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
		
		int moved = 0;
		for(EntityItem eitem : found){
			Vector3.setEntityMotionFromVector(eitem, x, y,z,0.4F);
			moved++;
		}
		
		List<EntityXPOrb> foundExp =  world.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.fromBounds(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

		for(EntityXPOrb eitem : foundExp){
			Vector3.setEntityMotionFromVector(eitem, x, y,z,0.4F);
			moved++;
		}
		
		
		if(moved > 0){
			this.onCastSuccess(world, player, pos);
		}
	}
}
