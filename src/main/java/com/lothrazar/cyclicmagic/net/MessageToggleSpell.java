package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MessageToggleSpell implements IMessage, IMessageHandler<MessageToggleSpell, IMessage>{

	private int spell_id;
	private static final String NBT_SPELL = "spell";
	public static final int ID = 1;

	public MessageToggleSpell(){
	}

	public MessageToggleSpell(int spell){

		spell_id = spell;
	}

	@Override
	public void fromBytes(ByteBuf buf){

		NBTTagCompound tags = ByteBufUtils.readTag(buf);
		spell_id = tags.getInteger(NBT_SPELL);
	}

	@Override
	public void toBytes(ByteBuf buf){

		NBTTagCompound tags = new NBTTagCompound();

		tags.setInteger(NBT_SPELL, spell_id);
		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(MessageToggleSpell message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);

		ItemCyclicWand.Spells.toggleSpell(wand, message.spell_id);

		return null;
	}
}
