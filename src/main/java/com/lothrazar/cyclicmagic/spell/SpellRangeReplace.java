package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.net.MessageSpellReplacer;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangeReplace extends BaseSpellRange {

	public SpellRangeReplace(int id, String n) {

		super.init(id, n);
		this.cost = 30;
		this.cooldown = 5;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellReplacer(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}
		return false;
	}

	public void castFromServer(BlockPos posMouseover, EnumFacing side, EntityPlayer player) {

		ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(player);
		if (heldWand == null) { return; }
		World world = player.worldObj;

		IBlockState stateHere = world.getBlockState(posMouseover);
		if (stateHere == null || stateHere.getBlock() == null) { return; }

		if (world.getTileEntity(posMouseover) != null) { return;// not chests, etc
		}

		Block blockHere = stateHere.getBlock();

		if (blockHere.getBlockHardness(stateHere, world, posMouseover) == -1) { return; // is
		                                                                                // unbreakable->
		                                                                                // like
		                                                                                // bedrock
		}

		int itemSlot = InventoryWand.getSlotByBuildType(heldWand, world.getBlockState(posMouseover));
		ItemStack[] invv = InventoryWand.readFromNBT(heldWand);
		ItemStack toPlace = InventoryWand.getFromSlot(heldWand, itemSlot);

		if (toPlace == null || toPlace.getItem() == null || Block.getBlockFromItem(toPlace.getItem()) == null) { return; }

		IBlockState placeState = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());

		if (placeState.getBlock() == blockHere && blockHere.getMetaFromState(stateHere) == toPlace.getMetadata()) {

		return;// dont replace cobblestone with cobblestone
		}

		if (world.destroyBlock(posMouseover, true) && world.setBlockState(posMouseover, placeState)) {

			if (player.capabilities.isCreativeMode == false) {

				invv[itemSlot].stackSize--;
				InventoryWand.writeToNBT(heldWand, invv);
			}

			this.playSound(world, placeState.getBlock(), posMouseover);
			this.spawnParticle(world, player, posMouseover);

			return;
		}
	}
}
