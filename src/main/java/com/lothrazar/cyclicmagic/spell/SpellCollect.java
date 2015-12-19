package com.lothrazar.cyclicmagic.spell;

import java.util.List;
import com.lothrazar.cyclicmagic.util.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellCollect extends BaseSpellExp {

	private final int h_radius = 20;
	private final int v_radius = 4;
	private final float speed = 1.2F;

	// TODO: potion effect like this? but with much less speed? 
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		int x = pos.getX(), y = pos.getY(), z = pos.getZ();

		List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.fromBounds(x - h_radius, y - v_radius, z - h_radius, x + h_radius, y + v_radius, z + h_radius));

		int moved = 0;
		for (EntityItem eitem : found) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, speed);
			moved++;
		}

		List<EntityXPOrb> foundExp = world.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.fromBounds(x - h_radius, y - v_radius, z - h_radius, x + h_radius, y + v_radius, z + h_radius));

		for (EntityXPOrb eitem : foundExp) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, speed);
			moved++;
		}

		return (moved > 0);
	}
}
