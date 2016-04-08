package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.util.UtilInventorySort;

import net.minecraft.inventory.ContainerWorkbench;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
public class OpenCraftingPacket  implements IMessage , IMessageHandler<OpenCraftingPacket, IMessage>
{
	NBTTagCompound tags = new NBTTagCompound(); 
	public OpenCraftingPacket(){}
	public OpenCraftingPacket(NBTTagCompound ptags)
	{
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.tags);
	}
	
	@Override
	public IMessage onMessage(OpenCraftingPacket message, MessageContext ctx)
	{
		System.out.println("OpenCraftingPacket attempt" );
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		if(p.openContainer != null ){
			p.closeScreen();
		}
		
		EntityPlayerMP player = (EntityPlayerMP)p;
		
        player.getNextWindowId();

        /**
         * Displays a GUI by ID. In order starting from id 0: Chest, Workbench, Furnace, Dispenser, Enchanting table,
         * Brewing stand, Villager merchant, Beacon, Anvil, Hopper, Dropper, Horse
         */
        // (int windowIdIn, String inventoryTypeIn, ITextComponent windowTitleIn, int slotCountIn, int entityIdIn)
        player.playerNetServerHandler.sendPacket(new SPacketOpenWindow(player.currentWindowId, "Crafting", p.getDisplayName()));
        player.openContainer = new ContainerWorkbench(player.inventory, player.worldObj, p.getPosition());
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.onCraftGuiOpened(player);
       // player.openContainer.addCraftingToCrafters(player);
		
		return null;
	}
}
