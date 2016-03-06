package com.lothrazar.cyclicmagic.net;

import java.util.List;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand.Spells;
import com.lothrazar.cyclicmagic.spell.ISpell;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MessageToggleVariant implements IMessage, IMessageHandler<MessageToggleVariant, IMessage>{
 
	public MessageToggleVariant(){

	}
 

	@Override
	public void fromBytes(ByteBuf buf){

	}

	@Override
	public void toBytes(ByteBuf buf){

		NBTTagCompound tags = new NBTTagCompound(); 
		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(MessageToggleVariant message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;

		ItemCyclicWand.Variant.toggle(player.getHeldItem());


		List<Integer> active = ItemCyclicWand.Variant.getSpellsFromVariant(ItemCyclicWand.Variant.getVariantFromMeta(player.getHeldItem()));
		int spellId;
		for(ISpell s : SpellRegistry.getSpellbook()){
			spellId = s.getID();
			Spells.unlockSpell(player.getHeldItem(), spellId, active.contains(spellId));
		}
		
		System.out.println("JUST TESTING MAX");
		
		ItemCyclicWand.Energy.increaseMaximum(player.getHeldItem(), 50);
		
		return null;
	}
}
