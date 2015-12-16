package com.lothrazar.cyclicmagic.net;
  
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageKeyToggle implements IMessage, IMessageHandler<MessageKeyToggle, IMessage>
{
	public MessageKeyToggle()
	{ 
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}
	
	@Override
	public IMessage onMessage(MessageKeyToggle message, MessageContext ctx)
	{  
		EntityPlayer player = ctx.getServerHandler().playerEntity; 
		PlayerPowerups props = PlayerPowerups.get(player);
	
		//www.minecraftforge.net/forum/index.php/topic,20135.0.html
		 
		int next = props.getSpellToggleNext();
		props.setSpellToggle(next);
		
		UtilSound.playSoundAt(player, "random.click");
 
		return null;
	}
}
 
