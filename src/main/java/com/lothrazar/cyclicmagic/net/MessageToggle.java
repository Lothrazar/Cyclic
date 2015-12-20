package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.SpellCaster;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MessageToggle implements IMessage, IMessageHandler<MessageToggle, IMessage> {
 
	private NBTTagCompound tags = null;
	private int spell_id;
	private static final String NBT_SPELL = "spell"; 

	public MessageToggle() {
	}

	public MessageToggle(int spell) {
		spell_id = spell;

		this.toNBT();
	}

	private void toNBT() {
		tags = new NBTTagCompound();

		tags.setInteger(NBT_SPELL, spell_id);// DUNSWE
	
	}

	private void fromNBT() {
 
		spell_id = tags.getInteger(NBT_SPELL);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tags = ByteBufUtils.readTag(buf);
		this.fromNBT();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		this.toNBT();
		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(MessageToggle message, MessageContext ctx) {

		message.fromNBT();

		EntityPlayer player = ctx.getServerHandler().playerEntity;
	 
 
		SpellCaster.toggleUnlock(player,message.spell_id);
	 
		return null;
	}
}
