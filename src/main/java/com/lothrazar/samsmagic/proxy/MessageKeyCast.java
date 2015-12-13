package com.lothrazar.samsmagic.proxy;
  
import com.lothrazar.samsmagic.ModSpells;
import com.lothrazar.samsmagic.SpellRegistry;  
import com.lothrazar.samsmagic.PlayerPowerups;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf; 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class MessageKeyCast implements IMessage, IMessageHandler<MessageKeyCast, IMessage>
{
	private BlockPos pos;
	private String csv;
	public static final int ID = 0;
	public MessageKeyCast()
	{ 
	}
	
	public MessageKeyCast(BlockPos pm)
	{ 
		pos = pm;
		csv = ModSpells.posToCSV(pos);
	}
	@Override
	public void fromBytes(ByteBuf buf)
	{
		//http://www.minecraftforge.net/forum/index.php?topic=20135.0

		csv = ByteBufUtils.readUTF8String(buf); 
        
		pos = ModSpells.stringCSVToBlockPos(csv);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
        ByteBufUtils.writeUTF8String(buf, csv);
	}
	
	@Override
	public IMessage onMessage(MessageKeyCast message, MessageContext ctx)
	{  
		System.out.println("CAST "+message.csv);
	
		//System.out.println("pos  "+message.pos.getX()+"::"+message.pos.getZ());
 
		EntityPlayer player = ctx.getServerHandler().playerEntity; 
		//PlayerPowerups props = PlayerPowerups.get(player);


		//www.minecraftforge.net/forum/index.php/topic,20135.0.html
	  
	//	if(props.getSpellToggle() != SpellRegistry.SPELL_TOGGLE_HIDE)
		//{
			SpellRegistry.cast(SpellRegistry.getPlayerCurrentISpell(player), player.worldObj, player, message.pos);
		//}

		return null;
	}
}
 
