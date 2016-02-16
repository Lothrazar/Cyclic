package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.SpellRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class MessageKeyRight implements IMessage, IMessageHandler<MessageKeyRight, IMessage>{

	public MessageKeyRight(){

	}

	public MessageKeyRight(BlockPos pm){

	}

	@Override
	public void fromBytes(ByteBuf buf){

	}

	@Override
	public void toBytes(ByteBuf buf){

	}

	@Override
	public IMessage onMessage(MessageKeyRight message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		// PlayerPowerups props = PlayerPowerups.get(player);
		// www.minecraftforge.net/forum/index.php/topic,20135.0.html
		if(SpellRegistry.spellsEnabled(player)){
			SpellRegistry.caster.shiftRight(player);
		}
		return null;
	}
}
