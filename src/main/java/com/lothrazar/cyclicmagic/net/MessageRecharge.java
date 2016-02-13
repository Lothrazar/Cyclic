package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.SpellRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRecharge implements IMessage, IMessageHandler<MessageRecharge, IMessage> {

	public MessageRecharge() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	@Override
	public IMessage onMessage(MessageRecharge message, MessageContext ctx) {

		SpellRegistry.caster.rechargeWithExp(ctx.getServerHandler().playerEntity);
		
		return null;
	}
}
