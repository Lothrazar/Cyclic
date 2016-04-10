package com.lothrazar.cyclicmagic.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import com.lothrazar.cyclicmagic.item.ItemEnderBook;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWarpButton implements IMessage, IMessageHandler<PacketWarpButton, IMessage> {
	public static final int	ID	= 26;
	public int							slot;

	public PacketWarpButton() {}

	public PacketWarpButton(int s) {
		slot = s;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.slot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
	}

	@Override
	public IMessage onMessage(PacketWarpButton message, MessageContext ctx) {
		EntityPlayer player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;

		int cost = (int) ItemRegistry.expCostPerTeleport;

		if (cost != 0 && UtilExperience.getExpTotal(player) < cost) {
			player.addChatMessage(new TextComponentTranslation(I18n.translateToLocal("gui.chatexp")));
		}
		else {
			ItemEnderBook.teleport(player, message.slot);
		}

		return null;
	}
}
