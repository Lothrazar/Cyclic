package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MessageToggleSpellGroup implements IMessage, IMessageHandler<MessageToggleSpellGroup, IMessage>{

	private String group;
	private static final String NBT_SPELL = "spell";

	public MessageToggleSpellGroup(){

	}

	public MessageToggleSpellGroup(String g){

		group = g;
	}

	@Override
	public void fromBytes(ByteBuf buf){

		NBTTagCompound tags = ByteBufUtils.readTag(buf);
		group = tags.getString(NBT_SPELL);
	}

	@Override
	public void toBytes(ByteBuf buf){

		NBTTagCompound tags = new NBTTagCompound();
		tags.setString(NBT_SPELL, group);
		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(MessageToggleSpellGroup message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;

		ItemCyclicWand.Spells.toggleSpellGroup(player.getHeldItem(), message.group);

		return null;
	}
}
