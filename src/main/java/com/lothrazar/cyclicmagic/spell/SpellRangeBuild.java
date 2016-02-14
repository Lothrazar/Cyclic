package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageSpellReach;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangeBuild extends BaseSpellRange {

	public SpellRangeBuild(int id, String n) {
		super.init(id, n);
		this.cost = 5;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);
			BlockPos offset = ModMain.proxy.getBlockMouseoverOffset(maxRange);

			if (mouseover != null && offset != null) {

				ModMain.network.sendToServer(new MessageSpellReach(mouseover, offset));
			}
		}

		return false;
	}

	public void castFromServer(BlockPos posMouseover, BlockPos posOffset, EntityPlayer p) {
		ItemStack heldWand = p.getHeldItem();
		if (heldWand == null || heldWand.getItem() instanceof ItemCyclicWand == false) {
			return;
		}

		int itemSlot = InventoryWand.getSlotByBuildType(heldWand, p.worldObj.getBlockState(posMouseover));
		ItemStack[] invv = InventoryWand.readFromNBT(heldWand);
		ItemStack toPlace = InventoryWand.getFromSlot(heldWand, itemSlot);

		if (toPlace != null && toPlace.getItem() != null && Block.getBlockFromItem(toPlace.getItem()) != null) {

			IBlockState state = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());

			if (state != null) {


				SpellRegistry.caster.castSuccess(this, p.worldObj, p, posOffset);

				// p.worldObj.setBlockState(pos, state);

				if (placeStateSafe(p.worldObj, p, posOffset, state)) {

					if (state.getBlock().stepSound != null && state.getBlock().stepSound.getBreakSound() != null) {
						UtilSound.playSoundAt(p, state.getBlock().stepSound.getPlaceSound());
					}

					if (p.capabilities.isCreativeMode == false) {
						invv[itemSlot].stackSize--;
						// player.inventoryContainer.detectAndSendChanges();
						InventoryWand.writeToNBT(heldWand, invv);
					}

					// yes im spawning particles on the server side, but the
					// util handles that
					this.spawnParticle(p.worldObj, p, posMouseover);
					this.playSound(p.worldObj, state.getBlock(), posOffset);
					
				}
			}
		}
	}

	private boolean placeStateSafe(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState) {
		if (world.isAirBlock(placePos) == false) {

			// if there is a block here, we might have to stop
			Block blockHere = world.getBlockState(placePos).getBlock();
			if (blockHere.isReplaceable(world, placePos) == false) {
				// for example, torches, and the top half of a slab if you click
				// in the empty space
				return false;
			}

			// ok its a soft block so try to break it first try to destroy it
			// first
			// unless it is liquid, don't try to destroy liquid
			if (blockHere.getMaterial() != Material.water && blockHere.getMaterial() != Material.lava) {
				boolean dropBlock = true;
				world.destroyBlock(placePos, dropBlock);
			}
		}

		// either it was air, or it wasnt and we broke it
		return world.setBlockState(placePos, placeState);
	}
}
