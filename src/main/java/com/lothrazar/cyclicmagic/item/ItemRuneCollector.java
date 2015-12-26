package com.lothrazar.cyclicmagic.item;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.Vector3;

public class ItemRuneCollector extends RuneBaseAbstract {
	public ItemRuneCollector() {
		super();
	}

	private final static int h_radius = 20;
	private final static int v_radius = 4;
	private final static float speed = 1.2F;

	@Override
	protected boolean trigger(World world,Entity entityIn ) {
		BlockPos pos = entityIn.getPosition();
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

	@Override
	protected int getCost() {
		return 0;
	}
}
