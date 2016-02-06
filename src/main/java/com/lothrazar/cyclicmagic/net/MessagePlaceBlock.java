package com.lothrazar.cyclicmagic.net;

//import com.lothrazar.util.Util;

import com.lothrazar.cyclicmagic.util.UtilParticle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlaceBlock implements IMessage, IMessageHandler<MessagePlaceBlock, IMessage> {
	private int x;
	private int y;
	private int z;
	private BlockPos pos;
	private ItemStack toPlace;

	public MessagePlaceBlock() {
	}

	public MessagePlaceBlock(BlockPos p, ItemStack is) {
		x = p.getX();
		y = p.getY();
		z = p.getZ();
		pos = p;
		toPlace = is;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		
		NBTTagCompound tags =  ByteBufUtils.readTag(buf);
		
		//NBTTagCompound itemTag = (NBTTagCompound) tags.getTag("stack");
		//toPlace = new ItemStack(Blocks.air);
		//toPlace.readFromNBT(itemTag);
		
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
		//NBTTagCompound itemTag = new NBTTagCompound();//
		
		//itemTag = toPlace.writeToNBT(itemTag);
		
		//tags.setTag("stack", tags);

		ByteBufUtils.writeTag(buf, tags);
		//ByteBufUtils.writeUTF8String(buf, x + "," + y + "," + z);
	}

	@Override
	public IMessage onMessage(MessagePlaceBlock message, MessageContext ctx) {
		
		if (ctx.side.isServer()) {
			// http://www.minecraftforge.net/forum/index.php?topic=21195.0
		
			EntityPlayer p = ctx.getServerHandler().playerEntity;
			
			//int itemSlot = p.inventory.currentItem + 1;
	
			//if(itemSlot < 9 && p.inventory.getStackInSlot(itemSlot) != null){
				
				//ItemStack toUse = p.inventory.getStackInSlot(itemSlot);
				//toPlace.toString()+""
				System.out.println("try place "+message.pos.toString());
			//}
			
		}

		return null;
	}
}
