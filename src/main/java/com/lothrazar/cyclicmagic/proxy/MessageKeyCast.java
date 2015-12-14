package com.lothrazar.cyclicmagic.proxy;
  
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf; 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class MessageKeyCast implements IMessage, IMessageHandler<MessageKeyCast, IMessage>
{
	private BlockPos pos;
	private EnumFacing side;
	private int entity;
	//private String csv;
	public static final int ID = 0;
	private NBTTagCompound tags = new NBTTagCompound();
	public MessageKeyCast()
	{ 
	}
	
	public MessageKeyCast(BlockPos pm,EnumFacing side,int entity)
	{ 
		tags.setString("pos", ModMain.posToCSV(pm));
		if(side == null){
			tags.setInteger("side", -1);//DUNSWE
		}
		else{
			tags.setInteger("side", side.getIndex());//DUNSWE
		}
		tags.setInteger("entity", entity);
		//TODO: convert to using NBT
		//pos = pm;
		//csv = ;
	}
	@Override
	public void fromBytes(ByteBuf buf)
	{
		tags = ByteBufUtils.readTag(buf);
		
		//http://www.minecraftforge.net/forum/index.php?topic=20135.0

		//csv = ByteBufUtils.readUTF8String(buf); 
       // 
		pos = ModMain.stringCSVToBlockPos(tags.getString("csv"));
		
		int iside = tags.getInteger("side");
		if(iside < 0){
			side = null;
		}
		else{
			side = EnumFacing.getFront(iside);
		}
		
		entity = tags.getInteger("entity");
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
       // ByteBufUtils.writeUTF8String(buf, csv);
		ByteBufUtils.writeTag(buf, this.tags);
	}
	
	@Override
	public IMessage onMessage(MessageKeyCast message, MessageContext ctx)
	{  
		System.out.println("CAST "+message.pos.toString());
	
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
 
