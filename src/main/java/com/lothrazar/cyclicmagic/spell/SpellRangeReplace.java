package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.PacketReplaceBlock;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellRangeReplace extends BaseSpellRange {

	public SpellRangeReplace(int id, String n) {
		super.init(id, n);
	}

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new PacketReplaceBlock(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
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

		if (world.getTileEntity(posMouseover) != null) { return; }

		Block blockHere = stateHere.getBlock();
 
		//if (blockHere.getBlockHardness(stateHere, world, posMouseover) == -1) { 
		if (stateHere.getBlockHardness(world, posMouseover) == -1) {  return;  }
 
		int itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
//		int itemSlot = InventoryWand.getSlotByBuildType(heldWand, world.getBlockState(posMouseover));
		ItemStack[] invv = InventoryWand.readFromNBT(heldWand);
		ItemStack toPlace = InventoryWand.getFromSlot(heldWand, itemSlot);

		if (toPlace == null || toPlace.getItem() == null || Block.getBlockFromItem(toPlace.getItem()) == null) { 
			UtilChat.addChatMessage(player, "wand.inventory.empty");
			return; 
		}
		IBlockState placeState = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());

		if (placeState.getBlock() == blockHere && blockHere.getMetaFromState(stateHere) == toPlace.getMetadata()) {

			return;// dont replace cobblestone with cobblestone
		}
		
		boolean didRePlace = false;
		
		try{
			didRePlace = world.destroyBlock(posMouseover, true) && world.setBlockState(posMouseover, placeState);
		}
		catch(java.util.ConcurrentModificationException e){
			ModMain.logger.warn(e.getLocalizedMessage());
			/*java.util.ConcurrentModificationException
				at java.util.HashMap$HashIterator.nextNode(Unknown Source) ~[?:1.8.0_91]
				at java.util.HashMap$KeyIterator.next(Unknown Source) ~[?:1.8.0_91]
				at net.minecraft.entity.EntityTracker.updateTrackedEntities(EntityTracker.java:283) ~[EntityTracker.class:?]
				at net.minecraft.server.MinecraftServer.updateTimeLightAndEntities(MinecraftServer.java:793) ~[MinecraftServer.class:?]
				at net.minecraft.server.MinecraftServer.tick(MinecraftServer.java:685) ~[MinecraftServer.class:?]
				at net.minecraft.server.integrated.IntegratedServer.tick(IntegratedServer.java:155) ~[IntegratedServer.class:?]
				at net.minecraft.server.MinecraftServer.run(MinecraftServer.java:534) [MinecraftServer.class:?]
			*/
		}
		if (didRePlace) {
			if (player.capabilities.isCreativeMode == false) {
				invv[itemSlot].stackSize--;
				InventoryWand.writeToNBT(heldWand, invv);
			}

			ItemCyclicWand.BuildType.setNextSlot(heldWand);
			this.playSound(world, player,placeState.getBlock(), posMouseover);
			this.spawnParticle(world, player, posMouseover);
		}
	}
}
