package com.lothrazar.cyclicmagic.potion;

import java.util.List;

import com.lothrazar.cyclicmagic.util.Vector3;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionMagnet extends PotionCustom{

	private final static int	ITEM_HRADIUS	= 20;
	private final static int	ITEM_VRADIUS	= 4;
	private final static float	ITEMSPEED		= 1.2F;
	
	public PotionMagnet(String name, boolean b, int potionColor) {
		super(name, b, potionColor); 
	}


	public  void tick(EntityLivingBase entityLiving) {

		World world = entityLiving.worldObj;

		BlockPos pos = entityLiving.getPosition();
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();

		List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));

		// int moved = 0;
		for (EntityItem eitem : found) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
			// moved++;
		}

		List<EntityXPOrb> foundExp = world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));

		for (EntityXPOrb eitem : foundExp) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
			// moved++;
		}
	}
}
