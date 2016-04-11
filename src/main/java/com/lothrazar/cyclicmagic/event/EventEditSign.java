package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EventEditSign {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickBlock event) {

		EntityPlayer entityPlayer = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		if (pos == null) { return; }
		//if (entityPlayer.isSneaking()) { return; }

		ItemStack held = event.getItemStack();//entityPlayer.getHeldItem(event.getHand());

		TileEntity tile = worldObj.getTileEntity(pos);

		// test
		if (held == null && tile instanceof TileEntitySign) {

			TileEntitySign sign = (TileEntitySign) tile;
			// sign.setEditable(true);
			ReflectionHelper.setPrivateValue(TileEntitySign.class, sign, true, "isEditable", "field_145916_j");
			sign.setPlayer(entityPlayer);

			entityPlayer.openEditSign(sign);
		}
	}
}
