package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.ModMain;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageOpenSpellbook implements IMessage, IMessageHandler<MessageOpenSpellbook, IMessage> {
 

	public MessageOpenSpellbook() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(MessageOpenSpellbook message, MessageContext ctx) {

		if(ctx.side.isClient()){

			//ctx.getClientHandler() exists but has nothing useful
			//should only have been sent from server to client anyway
			ModMain.proxy.displayGuiSpellbook();
		}
		return null;
	}
}
