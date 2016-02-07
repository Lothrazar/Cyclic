package com.lothrazar.cyclicmagic.net;

//import com.lothrazar.util.Util;

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
				
				int itemSlot = p.inventory.currentItem + 1;
				//9 is hotbar size
				if(itemSlot < 9 && p.inventory.getStackInSlot(itemSlot) != null){
					
					ItemStack toPlace = p.inventory.getStackInSlot(itemSlot);
					
					if(toPlace != null 
							&& toPlace.getItem() != null && 
							Block.getBlockFromItem(toPlace.getItem()) != null){
						
						IBlockState state = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());
						
						if(state != null){
							p.worldObj.setBlockState(message.pos, state);
							
							if(p.capabilities.isCreativeMode == false){
								p.inventory.decrStackSize(itemSlot, 1);
							}
						}
					}
				}
			}
		}
		return null;
	}
}
