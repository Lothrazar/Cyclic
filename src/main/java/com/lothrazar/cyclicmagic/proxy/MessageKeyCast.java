package com.lothrazar.cyclicmagic.proxy;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellCaster;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class MessageKeyCast implements IMessage, IMessageHandler<MessageKeyCast, IMessage> {
	private BlockPos pos;
	private EnumFacing side;
	private int entity;
	// private String csv;
	public static final int ID = 0;
	private NBTTagCompound tags = null;
	private static final String NBT_POS = "pos";
	private static final String NBT_ENTITY = "entity";
	private static final String NBT_SIDE = "side";

	public MessageKeyCast() {
	}

	public MessageKeyCast(BlockPos pm, EnumFacing pside, int pentity) {
		System.out.println("message constructor at x = "+pm.getX());
		pos = pm;
		side = pside;
		entity = pentity;

		this.toNBT();
	}

	private void toNBT() {
		tags = new NBTTagCompound();
		tags.setString(NBT_POS, UtilNBT.posToStringCSV(pos));
		
		System.out.println("to NBT ::: "+  UtilNBT.posToStringCSV(pos));
		
		if (side == null) {
			tags.setInteger(NBT_SIDE, -1);// DUNSWE
		} else {
			tags.setInteger(NBT_SIDE, side.getIndex());// DUNSWE
		}
		tags.setInteger(NBT_ENTITY, entity);
	}

	private void fromNBT() {
		// http://www.minecraftforge.net/forum/index.php?topic=20135.0
		String csv = tags.getString(NBT_POS);
		System.out.println("fromNBT : " + csv);
		if (csv == "") {
			pos = null;
		} else {
			pos = UtilNBT.stringCSVToBlockPos(csv);
		}

		int iside = tags.getInteger(NBT_SIDE);
		if (iside < 0) {
			side = null;
		} else {
			side = EnumFacing.getFront(iside);
		}

		entity = tags.getInteger(NBT_ENTITY);
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
	public IMessage onMessage(MessageKeyCast message, MessageContext ctx) {
		
		message.fromNBT();
		if(message.pos == null){
			System.out.println("ERROR: IS NULL POS");
			return null;
		}
		System.out.println("CAST " + message.pos.toString());
		System.out.println("side " + message.side);
		System.out.println("entity " + message.entity);

		// System.out.println("pos  "+message.pos.getX()+"::"+message.pos.getZ());

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		// PlayerPowerups props = PlayerPowerups.get(player);

		// www.minecraftforge.net/forum/index.php/topic,20135.0.html

		// if(props.getSpellToggle() != SpellRegistry.SPELL_TOGGLE_HIDE)
		// {
		SpellCaster.cast(SpellCaster.getPlayerCurrentISpell(player), player.worldObj, player, message.pos,message.side,message.entity);
		// }

		return null;
	}
}
