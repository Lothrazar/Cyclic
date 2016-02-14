package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.SpellRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSpellReach implements IMessage, IMessageHandler<MessageSpellReach, IMessage>{

	private BlockPos pos;
	private BlockPos posOffset;

	public MessageSpellReach(){

	}

	public MessageSpellReach(BlockPos mouseover, BlockPos offset){

		pos = mouseover;
		posOffset = offset;
	}

	@Override
	public void fromBytes(ByteBuf buf){

		NBTTagCompound tags = ByteBufUtils.readTag(buf);

		int x = tags.getInteger("x");
		int y = tags.getInteger("y");
		int z = tags.getInteger("z");
		pos = new BlockPos(x, y, z);

		x = tags.getInteger("ox");
		y = tags.getInteger("oy");
		z = tags.getInteger("oz");
		posOffset = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf){

		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("x", pos.getX());
		tags.setInteger("y", pos.getY());
		tags.setInteger("z", pos.getZ());

		tags.setInteger("ox", posOffset.getX());
		tags.setInteger("oy", posOffset.getY());
		tags.setInteger("oz", posOffset.getZ());

		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(MessageSpellReach message, MessageContext ctx){

		if(ctx.side.isServer() && message != null && message.pos != null){

			EntityPlayer p = ctx.getServerHandler().playerEntity;

			// if( p.worldObj.getBlockState(message.pos).getBlock().isReplaceable(p.worldObj,
			// message.pos)){

			SpellRegistry.reach.castFromServer(message.pos, message.posOffset, p);

		}

		return null;
	}
}
