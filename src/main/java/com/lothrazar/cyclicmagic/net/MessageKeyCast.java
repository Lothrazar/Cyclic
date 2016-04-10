package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

public class MessageKeyCast implements IMessage, IMessageHandler<MessageKeyCast, IMessage> {

	private BlockPos						pos;
	private EnumFacing					side;
	// private String csv;
	private NBTTagCompound			tags			= null;
	private static final String	NBT_POS		= "pos";
	private static final String	NBT_SIDE	= "side";
	public static final int			ID				= 11;

	public MessageKeyCast() {

	}

	public MessageKeyCast(BlockPos pm, EnumFacing pside) {

		pos = pm;
		side = pside;

		this.toNBT();
	}

	private void toNBT() {

		tags = new NBTTagCompound();
		tags.setString(NBT_POS, UtilNBT.posToStringCSV(pos));

		if (side == null) {
			tags.setInteger(NBT_SIDE, -1);// DUNSWE
		}
		else {
			tags.setInteger(NBT_SIDE, side.getIndex());// DUNSWE
		}
	}

	private void fromNBT() {

		// http://www.minecraftforge.net/forum/index.php?topic=20135.0
		String csv = tags.getString(NBT_POS);

		if (csv == "") {
			pos = null;
		}
		else {
			pos = UtilNBT.stringCSVToBlockPos(csv);
		}

		int iside = tags.getInteger(NBT_SIDE);
		if (iside < 0) {
			side = null;
		}
		else {
			side = EnumFacing.getFront(iside);
		}
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

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		// PlayerPowerups props = PlayerPowerups.get(player);

		// www.minecraftforge.net/forum/index.php/topic,20135.0.html

		if (SpellRegistry.spellsEnabled(player)) {
			UtilSpellCaster.tryCastCurrent(player.worldObj, player, message.pos, message.side);
		}

		return null;
	}
}
