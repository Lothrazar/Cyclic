package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventFurnaceStardew  implements IFeatureEvent{

	private static boolean stardewFurnace;

	// inspired by stardew valley

	// http://minecraft.gamepedia.com/Furnace
	final static int	SLOT_INPUT	= 0;
	final static int	SLOT_FUEL		= 1;
	final static int	SLOT_OUTPUT	= 2;

	@SubscribeEvent
	public void onPlayerFurnace(PlayerInteractEvent event) {
		if(!stardewFurnace){return;}

		EntityPlayer entityPlayer = event.getEntityPlayer();
		// ignore in creative// left clicking just breaks it anyway
		if (entityPlayer.capabilities.isCreativeMode) { return; }

		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		if (pos == null) { return; }
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		int playerSlot = entityPlayer.inventory.currentItem;

		if (held == null) {
			held = entityPlayer.getHeldItemOffhand();
			// offhand slot is the highest number
			playerSlot = entityPlayer.inventory.getSizeInventory() - 1;
		}

		TileEntity tile = worldObj.getTileEntity(pos);

		if (tile instanceof TileEntityFurnace) {

			TileEntityFurnace furnace = (TileEntityFurnace) tile;

			if (held == null) {
				extractFurnaceOutput(furnace);
			}
			else if (isFuel(held)) {
				tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, SLOT_FUEL);
			}
			else if (canBeSmelted(held)) {

				tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, SLOT_INPUT);
			}

			// nope does not satisfy this
			// boolean inf = furnace instanceof IItemHandlerModifiable;
		}
	}

	private void tryMergeStackIntoSlot(TileEntityFurnace furnace, EntityPlayer entityPlayer, int playerSlot, int furnaceSlot) {

		ItemStack current = furnace.getStackInSlot(furnaceSlot);
		ItemStack held = entityPlayer.inventory.removeStackFromSlot(playerSlot);

		if (current == null) {
			// just done
			furnace.setInventorySlotContents(furnaceSlot, held.copy());

			held = null;
			entityPlayer.inventory.setInventorySlotContents(playerSlot, null);
		}
		else {

			// merging updates the stack size numbers in both furnace and in players
			// invo
			UtilInventory.mergeItemsBetweenStacks(held, current);

			// so now we just fix if something is size zero
			if (held.stackSize == 0) {
				held = null;
			}

			entityPlayer.inventory.setInventorySlotContents(playerSlot, held);

		}

		entityPlayer.inventory.markDirty();
	}

	private void extractFurnaceOutput(TileEntityFurnace furnace) {

		ItemStack current = furnace.removeStackFromSlot(SLOT_OUTPUT);
		if (current != null) {
			UtilEntity.dropItemStackInWorld(furnace.getWorld(), furnace.getPos(), current);
		}
	}

	private boolean canBeSmelted(ItemStack input) {

		// we literally get the smelt recipe instance to test if it has one
		ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(input);
		return (itemstack != null);
	}

	private boolean isFuel(ItemStack input) {

		// how long does it burn for? zero means it isnt fuel
		return TileEntityFurnace.getItemBurnTime(input) > 0;
	}

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.player; 
		
		stardewFurnace = config.getBoolean("Furnace Speed", category, true,
				"Quickly fill a furnace by hitting it with fuel or an item, or interact with an empty hand to pull out the results [Inspired by Stardew Valley]");

		
	}
}
