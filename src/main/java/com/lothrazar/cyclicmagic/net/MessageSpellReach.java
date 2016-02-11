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

public class MessageSpellReach implements IMessage, IMessageHandler<MessageSpellReach, IMessage> {
	private int x;
	private int y;
	private int z;
	private BlockPos pos;

	public MessageSpellReach() {
	}

	public MessageSpellReach(BlockPos p) {
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
	public IMessage onMessage(MessageSpellReach message, MessageContext ctx) {
		
		if (ctx.side.isServer() && message != null && message.pos != null) {

			EntityPlayer p = ctx.getServerHandler().playerEntity;
			
			if(p.worldObj.isAirBlock(message.pos) || p.worldObj.getBlockState(message.pos).getBlock().isReplaceable(p.worldObj, message.pos)){
				
				SpellRegistry.reach.castFromServer(message.pos, p);
			}
		}
		
		return null;
	}
}
