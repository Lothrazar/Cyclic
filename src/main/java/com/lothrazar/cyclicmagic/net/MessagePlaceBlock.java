package com.lothrazar.cyclicmagic.net;

import java.util.ArrayList;
import java.util.Random;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlaceBlock implements IMessage, IMessageHandler<MessagePlaceBlock, IMessage> {
	private int x;
	private int y;
	private int z;
	private BlockPos pos;

	public MessagePlaceBlock() {
	}

	public MessagePlaceBlock(BlockPos p) {
		x = p.getX();
		y = p.getY();
		z = p.getZ();
		pos = p;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		
		NBTTagCompound tags =  ByteBufUtils.readTag(buf);
	
		x = tags.getInteger("x");
		y = tags.getInteger("y");
		z = tags.getInteger("z");
		pos = new BlockPos(x,y,z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("x", x);
		tags.setInteger("y", y);
		tags.setInteger("z", z);

		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(MessagePlaceBlock message, MessageContext ctx) {
		
		if (ctx.side.isServer() && message != null && message.pos != null) {
			// http://www.minecraftforge.net/forum/index.php?topic=21195.0

			EntityPlayer p = ctx.getServerHandler().playerEntity;
			
			if(p.worldObj.isAirBlock(message.pos) || p.worldObj.getBlockState(message.pos).getBlock().isReplaceable(p.worldObj, message.pos)){
				
				int buildType = ItemCyclicWand.BuildType.getBuildType(p.getHeldItem());
				
				ItemStack[] inv = InventoryWand.readFromNBT(p.getHeldItem());
				ArrayList<Integer> slotNonEmpty = new ArrayList<Integer>();
				
				for(int i = 0; i < inv.length; i++){
					
					if(inv[i] != null && inv[i].getItem() != null && Block.getBlockFromItem(inv[i].getItem()) != null){
						slotNonEmpty.add(i);
					}
				}

				ItemStack toPlace = null;
				int itemSlot = -1;
				//brute forcing it. there is surely a more elegant way in each branch
				//TODO: switch?
				//TODO: move to Build Type subclass or something?
				if(buildType == ItemCyclicWand.BuildType.FIRST.ordinal()){
				
					for(int i = 0; i < inv.length; i++){
						if(inv[i] != null){
							toPlace = inv[i];
							
							itemSlot = i;
							break;
						}
					}
				}
				else if(buildType == ItemCyclicWand.BuildType.ROTATE.ordinal()){
					
					int rot = ItemCyclicWand.BuildType.getBuildRotation(p.getHeldItem());
				
					if(rot < 0 || rot >= inv.length){//JIT validation
						rot = 0;
					}
					int test = InventoryWand.INV_SIZE+2;//like aninfloop but with a max
					//in case we have gaps, maybe its [0,1,4] have items, so cycle through
					for(int i = 0; i < test; i++){
						
						if(inv[rot] != null){
							itemSlot = rot;
							toPlace = inv[itemSlot];
							
							rot++;
							if(rot >= inv.length){
								rot = 0;
							}
							
							ItemCyclicWand.BuildType.setBuildRotation(p.getHeldItem(),rot);
							
							break;
						}
					}
				}
				else if(buildType == ItemCyclicWand.BuildType.RANDOM.ordinal()){
					
					Random rand = new Random();
					itemSlot = slotNonEmpty.get(rand.nextInt(slotNonEmpty.size()));
					toPlace = inv[itemSlot];
				}
				
				if(toPlace != null //shouldnt be nul anymore
						&& toPlace.getItem() != null && 
						Block.getBlockFromItem(toPlace.getItem()) != null){
					
					IBlockState state = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());
					
					if(state != null && p.getHeldItem()!=null&&p.getHeldItem().getItem() instanceof ItemCyclicWand){
						
						SpellRegistry.reach.placeFromServerPacket(p, message.pos, state);
						
						if(p.capabilities.isCreativeMode == false){
							inv[itemSlot].stackSize--;
							
							InventoryWand.writeToNBT(p.getHeldItem(), inv);
						}
					}
				}
			}
		}
		
		return null;
	}
}
