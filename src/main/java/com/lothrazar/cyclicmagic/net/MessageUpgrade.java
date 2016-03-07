package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand.Energy;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilSound;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpgrade implements IMessage, IMessageHandler<MessageUpgrade, IMessage>{

	public static final int upgrade = 50;
	public MessageUpgrade(){

	}

	@Override
	public void fromBytes(ByteBuf buf){

	}

	@Override
	public void toBytes(ByteBuf buf){

	}

	@Override
	public IMessage onMessage(MessageUpgrade message, MessageContext ctx){

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemCyclicWand){
			
			if(Energy.UPGRADE_EXP_COST < UtilExperience.getExpTotal(player) && 
					ItemCyclicWand.Energy.getMaximumLargest() > ItemCyclicWand.Energy.getMaximum(player.getHeldItem())){

				ItemCyclicWand.Energy.increaseMaximum(player.getHeldItem(), upgrade);
				
				UtilExperience.drainExp(player, Energy.UPGRADE_EXP_COST);
				UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.fill);
			}
			else{
				UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.buzzp);
				
				UtilChat.message(player, StatCollector.translateToLocal("upgrade.notenough") + " "+  Energy.UPGRADE_EXP_COST);
			}
		}
		
		return null;
	}
}
