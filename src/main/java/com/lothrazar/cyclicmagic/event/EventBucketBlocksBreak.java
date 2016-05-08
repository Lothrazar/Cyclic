package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import com.lothrazar.cyclicmagic.block.TileEntityBucketStorage;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventBucketBlocksBreak implements IFeatureEvent{

	@SubscribeEvent
	public void onBreakEvent(BreakEvent event) {

		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = event.getState();
		TileEntity ent = world.getTileEntity(pos);

		// TODO; check tool/pickaxe? if notHarvestable or whatever, drop the
		// buckets and the ..glass?

		if (ent != null && ent instanceof TileEntityBucketStorage) {
			TileEntityBucketStorage t = (TileEntityBucketStorage) ent;
			ItemStack stack = new ItemStack(state.getBlock());

			UtilNBT.setItemStackNBT(stack, BlockBucketStorage.NBTBUCKETS, t.getBuckets());

			UtilEntity.dropItemStackInWorld(world, pos, stack);

			t.setBuckets(0);
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
