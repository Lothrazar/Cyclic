package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellBuilder extends BaseSpell {

	public SpellBuilder(int id, String n) {
		super(id, n);
		this.cooldown = 1;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		if (pos == null || side == null) {
			return false;
		}
		if (world.getBlockState(pos) == null || world.getBlockState(pos).getBlock() == null) {
			return false;
		}

		BlockPos placePos = pos.offset(side);

		IBlockState placeState = world.getBlockState(pos);
		int meta = placeState.getBlock().getMetaFromState(placeState);
		int slotFound = -1;
		ItemStack curr;
		for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			curr = player.inventory.getStackInSlot(i);

			if (curr != null && curr.getItem() == Item.getItemFromBlock(placeState.getBlock()) && curr.getMetadata() == meta) {
				slotFound = i;
				break;
			}
		}
		// damn too bad this is private
		// int slot =
		// player.inventory.getInventorySlotContainItem(Item.getItemFromBlock(placeState.getBlock()));
		//if (player.inventory.hasItem(Item.getItemFromBlock(placeState.getBlock()))) {

			if (world.setBlockState(placePos, placeState)) {
				// if it worked

				player.inventory.decrStackSize(slotFound, 1);
				return true;
			}
		//}

		return false;
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos) {

		// UtilSound.playSoundAt(player, UtilSound.toss);
		super.onCastSuccess(world, player, pos);
	}
}
